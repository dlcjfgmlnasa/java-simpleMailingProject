package 메일링프로젝트;

import static java.util.stream.Collectors.toList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class PanelOnTable extends JPanel {
	private static final long serialVersionUID = 1L;
	String[] searchBox = {"보낸이","주소","보낸이+주소","제목"};
	String[] columnName = {"읽음여부","보낸이","보낸이 주소","제목","날짜"};
	String[] sortBox = {"날짜","보낸이","제목"};
	String[] filterBox = {"모든 메일","안 읽은 메일","첨부메일","나에게 온 메일"};
	private final ArrayList<TableContents> originalList;
	private List<TableContents> contentsList = new ArrayList<TableContents>();
	private JTable jTable;
	private JScrollPane jScrollPane;
	private DefaultTableModel model = new DefaultTableModel(columnName, 0){
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int col) {
	        return false;
	}};
	private JSlider jSlider = new JSlider();
	private final int sliced = 20;
	private JTextField searchfield = new JTextField(30);
	private String e_mail = User.id+"@"+User.mail;

	
	
	private void TableSetting(JTable jTable){
		DefaultTableCellRenderer celAlignCenter = new DefaultTableCellRenderer();
		celAlignCenter.setHorizontalAlignment(JLabel.CENTER);
		DefaultTableCellRenderer celAlignRight = new DefaultTableCellRenderer();
		celAlignRight.setHorizontalAlignment(JLabel.RIGHT);
		
		jTable.getColumn("읽음여부").setPreferredWidth(5);
		jTable.getColumn("읽음여부").setCellRenderer(celAlignCenter);
		jTable.getColumn("보낸이").setPreferredWidth(40);
		jTable.getColumn("보낸이").setCellRenderer(celAlignCenter);
		jTable.getColumn("보낸이 주소").setPreferredWidth(100);
		jTable.getColumn("보낸이 주소").setCellRenderer(celAlignCenter);
		jTable.getColumn("제목").setPreferredWidth(300);
		jTable.getColumn("제목").setCellRenderer(celAlignCenter);
		jTable.getColumn("날짜").setPreferredWidth(100);
		jTable.getColumn("날짜").setCellRenderer(celAlignCenter);
		jTable.setRowHeight(30);
	}
	
	
	private void SliderSetting(JSlider jSlider){
		int division = contentsList.size() / sliced;
		jSlider.setMaximum(division);
		jSlider.setMinimum(1);
		jSlider.setValue(1);
		jSlider.setPaintTicks(true);
		jSlider.setPaintLabels(true);
		jSlider.setPaintTrack(true);
		jSlider.setMajorTickSpacing(1);
		jSlider.setSize(200, 50);
	}
	
	public PanelOnTable(Folder folder) throws MessagingException{
		setLayout(new BorderLayout());
		TableDBConnet cnDB = new TableDBConnet();
		cnDB.DBconnect();
		
		contentsList = cnDB.<TableContents>select("select * from content "
				+ "where foldername = '"+folder.getFullName()+"' and e_mail='"+e_mail+"'"
				+ " and passward='"+User.passwd+"';");
		
		
		
		JPanel jPanel = new JPanel(new BorderLayout());
		JPanel searchPanel = new JPanel(new FlowLayout());
		searchfield.setText("메일 검색");
		searchfield.setPreferredSize(new Dimension(5, 30));
		searchfield.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}
			public void focusGained(FocusEvent e) {
				searchfield.setText("");
			}
		});
		JButton jButton = new JButton("검색");
		
		JComboBox<String> searching = new JComboBox<>();
		for(String item : searchBox) searching.addItem(item);
		
		JComboBox<String> sorting = new JComboBox<>();
		for(String item : sortBox) sorting.addItem(item);
		
		JComboBox<String> filting = new JComboBox<>();
		for(String item : filterBox) filting.addItem(item);
		
		JButton sendButton = new JButton("메일 전송");
		sendButton.addActionListener(e -> {
			new SendEmail();
		});
		searchPanel.add(searching);
		searchPanel.add(searchfield);
		searchPanel.add(jButton);
		searchPanel.add(new JLabel("필터"));
		searchPanel.add(filting);
		searchPanel.add(new JLabel("정렬"));
		searchPanel.add(sorting);
		searchPanel.add(sendButton);
		jPanel.add(searchPanel,BorderLayout.NORTH);

		jTable = new JTable(model);
		jScrollPane = new JScrollPane(jTable);
		jSlider = new JSlider();
		TableSetting(jTable);
		
		
		SliderSetting(jSlider);
		
		contentsList.sort((o1,o2) -> {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date t1 = null,t2 =null;
			try{
				t1 = dateFormat.parse(o1.getTime());
				t2 = dateFormat.parse(o2.getTime());
			} catch(Exception e){}
			return t2.compareTo(t1);
		});
		originalList = (ArrayList<TableContents>) contentsList;
		
		int count=0;
		for(TableContents tc : contentsList){
			String read = tc.getReading() ? "읽음" : "읽지않음"; 
			model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
			if(sliced == ++count) break;
		}
		makeTableContents();
		
		

		
		//sorting 콤보박스 부분
		sorting.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			String sort = (String)cb.getSelectedItem();
			Comparator<TableContents> comparator = null;
			
			switch(sort){
				case "날짜" :
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					comparator = (o1,o2) -> {
						Date d1 = null, d2 = null;
						try{
							d1 = dateFormat.parse(o1.getTime());
							d2 = dateFormat.parse(o2.getTime());
						} catch(Exception e1){}
						return d2.compareTo(d1);	
					}; break;
				case "보낸이" :
					 comparator = (o1,o2) -> {
						 return o2.getSender().compareTo(o1.getSender());
					 }; break;
				case "제목" :
					 comparator = (o1,o2) -> {
						 return o2.getSubject().compareTo(o1.getSubject());
					 }; break;
			}		
			contentsList.sort(comparator);
			initTable();
		});
		
		//필터링 콤보박스 부분
		filting.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			ArrayList<TableContents> tempList = (ArrayList<TableContents>) contentsList;
			String filter = (String)cb.getSelectedItem();
			
			switch(filter){
				case "모든 메일" : {
					contentsList = originalList; 
				} break;
				case "안 읽은 메일" : {
					contentsList = tempList.stream()
									   .filter(t -> (!t.getReading()))
									   .collect(toList());
				} break;
				case "첨부메일" : {
					contentsList = tempList.stream()
									   .filter(t -> t.getAttach())
								       .collect(toList());
				} break;
				case "나에게 온 메일" : {
					contentsList = tempList.stream()
								   	   .filter(t -> t.getSenderURL().equals(e_mail))
								   	   .collect(toList());
				} break;
			}
			makeTableContents();
			initTable();
		});
		
		jButton.addActionListener(e ->{
			String key = (String)searching.getSelectedItem();
			String search = searchfield.getText();
			List<TableContents> tempList = new ArrayList<TableContents>();
			searchfield.setText("메일 검색");
			sorting.setSelectedItem("날짜");
			filting.setSelectedItem("모든 메일");
			switch(key){
				case "보낸이" : {
					tempList = originalList.stream()
										   .filter(t -> t.getSender().contains(search))
										   .collect(toList());
				}
				break;
				case "주소" :{
					tempList = originalList.stream()
										   .filter(t -> t.getSenderURL().contains(search))
										   .collect(toList());
				}; break;
				case "보낸이+주소" :{ 
					tempList = originalList.stream()
										   .filter(t -> (t.getSender()+t.getSenderURL()).contains(search))
										   .collect(toList());
				}
				; break;
				case "제목" :{
					tempList = originalList.stream()
										   .filter(t -> (t.getSubject()).contains(search))
										   .collect(toList());
				}; break;
			}
			contentsList = tempList;
			if("".equals(search) || "메일 검색".equals(search)) {
				contentsList = originalList;
			}
			makeTableContents();
			initTable();
		});
		

		
		jTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
			    if (e.getClickCount() == 2) { //내용 보기
			    	int fps = jSlider.getValue()-1;
			    	int row = jTable.rowAtPoint(e.getPoint());
			    	int index = row+fps*sliced;
			    	new ContentsView(contentsList.get(index));
			    }
			    if (e.getButton() == 3) { //답장하기
			    	HashMap<String, String> temp = new HashMap<>();
					int row = jTable.rowAtPoint(e.getPoint());
					int i=0;
					for(String column : columnName){
						temp.put(column,(String)jTable.getModel().getValueAt(row, i++));
					}
					new SendEmail(temp);
			    } 
			}
		});
		add(jPanel,BorderLayout.NORTH);
		add(jSlider,BorderLayout.SOUTH);
		add(jScrollPane,BorderLayout.CENTER);
		
	}
	
	private void initTable(){
		int totalCountNumber = contentsList.size();
		int fps = jSlider.getValue();
		int division = totalCountNumber / sliced;
		int remainder = totalCountNumber % sliced;

		model.setNumRows(0);
		if(totalCountNumber <= sliced){
			for(TableContents tc : contentsList){
				String read = tc.getReading() ? "읽음" : "읽지않음";
				model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
			}
		} else{
			int value  = ((fps == division) ? remainder : sliced);
			for(int i=(fps-1)*sliced ; i < (fps-1)*sliced + value ; i++){
				TableContents tc = contentsList.get(i);  
				String read = tc.getReading() ? "읽음" : "읽지않음";
				model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
			}
		}
	}
	
	private void makeTableContents(){
		int totalCountNumber = contentsList.size();
		int division = totalCountNumber / sliced;
		int remainder = totalCountNumber % sliced;
		final List<TableContents> tList = contentsList;
		jSlider.setMaximum(division);
		jSlider.setMinimum(1);
		
		jSlider.addChangeListener(e -> {
			model.setNumRows(0);
			if(tList.size()==0) return;
			JSlider source = (JSlider)e.getSource();
			if (!source.getValueIsAdjusting()) {
				int fps = (int)source.getValue();
				int value  = ((fps == division) ? remainder : sliced);
				if(totalCountNumber <= sliced){
					for(TableContents tc : contentsList){
						String read = tc.getReading() ? "읽음" : "읽지않음";
						model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
					}
				} else{
					for(int i=(fps-1)*sliced ; i < (fps-1)*sliced + value ; i++){
						try{
							TableContents tc = tList.get(i);
							String read = tc.getReading() ? "읽음" : "읽지않음";
							model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
						} catch(IndexOutOfBoundsException e1){}
					}
				}
		     }
		});
	}

}

