package ���ϸ�������Ʈ;

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
	String[] searchBox = {"������","�ּ�","������+�ּ�","����"};
	String[] columnName = {"��������","������","������ �ּ�","����","��¥"};
	String[] sortBox = {"��¥","������","����"};
	String[] filterBox = {"��� ����","�� ���� ����","÷�θ���","������ �� ����"};
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
		
		jTable.getColumn("��������").setPreferredWidth(5);
		jTable.getColumn("��������").setCellRenderer(celAlignCenter);
		jTable.getColumn("������").setPreferredWidth(40);
		jTable.getColumn("������").setCellRenderer(celAlignCenter);
		jTable.getColumn("������ �ּ�").setPreferredWidth(100);
		jTable.getColumn("������ �ּ�").setCellRenderer(celAlignCenter);
		jTable.getColumn("����").setPreferredWidth(300);
		jTable.getColumn("����").setCellRenderer(celAlignCenter);
		jTable.getColumn("��¥").setPreferredWidth(100);
		jTable.getColumn("��¥").setCellRenderer(celAlignCenter);
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
		searchfield.setText("���� �˻�");
		searchfield.setPreferredSize(new Dimension(5, 30));
		searchfield.addFocusListener(new FocusListener() {
			public void focusLost(FocusEvent e) {
			}
			public void focusGained(FocusEvent e) {
				searchfield.setText("");
			}
		});
		JButton jButton = new JButton("�˻�");
		
		JComboBox<String> searching = new JComboBox<>();
		for(String item : searchBox) searching.addItem(item);
		
		JComboBox<String> sorting = new JComboBox<>();
		for(String item : sortBox) sorting.addItem(item);
		
		JComboBox<String> filting = new JComboBox<>();
		for(String item : filterBox) filting.addItem(item);
		
		JButton sendButton = new JButton("���� ����");
		sendButton.addActionListener(e -> {
			new SendEmail();
		});
		searchPanel.add(searching);
		searchPanel.add(searchfield);
		searchPanel.add(jButton);
		searchPanel.add(new JLabel("����"));
		searchPanel.add(filting);
		searchPanel.add(new JLabel("����"));
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
			String read = tc.getReading() ? "����" : "��������"; 
			model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
			if(sliced == ++count) break;
		}
		makeTableContents();
		
		

		
		//sorting �޺��ڽ� �κ�
		sorting.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			String sort = (String)cb.getSelectedItem();
			Comparator<TableContents> comparator = null;
			
			switch(sort){
				case "��¥" :
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					comparator = (o1,o2) -> {
						Date d1 = null, d2 = null;
						try{
							d1 = dateFormat.parse(o1.getTime());
							d2 = dateFormat.parse(o2.getTime());
						} catch(Exception e1){}
						return d2.compareTo(d1);	
					}; break;
				case "������" :
					 comparator = (o1,o2) -> {
						 return o2.getSender().compareTo(o1.getSender());
					 }; break;
				case "����" :
					 comparator = (o1,o2) -> {
						 return o2.getSubject().compareTo(o1.getSubject());
					 }; break;
			}		
			contentsList.sort(comparator);
			initTable();
		});
		
		//���͸� �޺��ڽ� �κ�
		filting.addActionListener(e -> {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>)e.getSource();
			ArrayList<TableContents> tempList = (ArrayList<TableContents>) contentsList;
			String filter = (String)cb.getSelectedItem();
			
			switch(filter){
				case "��� ����" : {
					contentsList = originalList; 
				} break;
				case "�� ���� ����" : {
					contentsList = tempList.stream()
									   .filter(t -> (!t.getReading()))
									   .collect(toList());
				} break;
				case "÷�θ���" : {
					contentsList = tempList.stream()
									   .filter(t -> t.getAttach())
								       .collect(toList());
				} break;
				case "������ �� ����" : {
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
			searchfield.setText("���� �˻�");
			sorting.setSelectedItem("��¥");
			filting.setSelectedItem("��� ����");
			switch(key){
				case "������" : {
					tempList = originalList.stream()
										   .filter(t -> t.getSender().contains(search))
										   .collect(toList());
				}
				break;
				case "�ּ�" :{
					tempList = originalList.stream()
										   .filter(t -> t.getSenderURL().contains(search))
										   .collect(toList());
				}; break;
				case "������+�ּ�" :{ 
					tempList = originalList.stream()
										   .filter(t -> (t.getSender()+t.getSenderURL()).contains(search))
										   .collect(toList());
				}
				; break;
				case "����" :{
					tempList = originalList.stream()
										   .filter(t -> (t.getSubject()).contains(search))
										   .collect(toList());
				}; break;
			}
			contentsList = tempList;
			if("".equals(search) || "���� �˻�".equals(search)) {
				contentsList = originalList;
			}
			makeTableContents();
			initTable();
		});
		

		
		jTable.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
			    if (e.getClickCount() == 2) { //���� ����
			    	int fps = jSlider.getValue()-1;
			    	int row = jTable.rowAtPoint(e.getPoint());
			    	int index = row+fps*sliced;
			    	new ContentsView(contentsList.get(index));
			    }
			    if (e.getButton() == 3) { //�����ϱ�
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
				String read = tc.getReading() ? "����" : "��������";
				model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
			}
		} else{
			int value  = ((fps == division) ? remainder : sliced);
			for(int i=(fps-1)*sliced ; i < (fps-1)*sliced + value ; i++){
				TableContents tc = contentsList.get(i);  
				String read = tc.getReading() ? "����" : "��������";
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
						String read = tc.getReading() ? "����" : "��������";
						model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
					}
				} else{
					for(int i=(fps-1)*sliced ; i < (fps-1)*sliced + value ; i++){
						try{
							TableContents tc = tList.get(i);
							String read = tc.getReading() ? "����" : "��������";
							model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
						} catch(IndexOutOfBoundsException e1){}
					}
				}
		     }
		});
	}

}

