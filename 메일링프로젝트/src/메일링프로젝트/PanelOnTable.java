package 메일링프로젝트;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

class test{
	
}

public class PanelOnTable extends JPanel{
	String[] columnName = {"읽음여부","보낸이","보낸이 주소","제목","날짜"};
	private String DBid = "root";
	private String DBpasswd = "zxc2051801";
	private String DBURL = "jdbc:mysql://localhost/mail";
	private controlDB cnDB;
	private JTable jTable;
	private JScrollPane jScrollPane;
	private JSlider jSlider;
	private int sliced = 20;
	
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
	private void SliderSetting(JSlider jSlider,int totalCountNumber){
		int division = totalCountNumber / sliced;
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
		HashMap<String, String> mail = new HashMap<>();
		setLayout(new BorderLayout());
		MessageList mList = new MessageList();
		int totalCountNumber = 0;
		List<TableContents> tempList = new ArrayList<TableContents>();
		DefaultTableModel model = new DefaultTableModel(columnName, 0){
		    public boolean isCellEditable(int row, int col) {
		        return false;
		}};

		jTable = new JTable(model);
		jScrollPane = new JScrollPane(jTable);
		jSlider = new JSlider();
		TableSetting(jTable);
		
		
		String tableName = User.id+"_"+User.mail.split("\\.")[0]+"table";
		
		try{
			cnDB = new MYSQLMailcontrol(DBid, DBpasswd, DBURL);
			cnDB.DBconnet();
			totalCountNumber = cnDB.count("select count(*) from "+tableName+" where folderName='"+
									folder.getFullName()+"'");
			
			tempList = cnDB.select("select * from "+tableName+" where folderName='"+folder.getFullName()+"';");
			tempList.sort((o1,o2)->{
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date t1 = null,t2 =null;
				try{
					t1 = dateFormat.parse(o1.getTime());
					t2 = dateFormat.parse(o2.getTime());
				} catch (Exception e) {	
				}
				return (t2.compareTo(t1));
			});
			int count=0;
			for(TableContents tc : tempList){
				String read = tc.getReading() ? "읽음" : "읽지않음"; 
				model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
				if(sliced == ++count) break;
			}
			
			SliderSetting(jSlider,totalCountNumber);
			int divison = totalCountNumber / sliced;
			int remainder = totalCountNumber % sliced;
			final List<TableContents> tList = tempList;
			jSlider.addChangeListener(e -> {
				model.setNumRows(0);
				JSlider source = (JSlider)e.getSource();
		        if (!source.getValueIsAdjusting()) {
		            int fps = (int)source.getValue();
		            int value = (fps == divison) ? remainder : sliced;
		            for(int i=(fps-1)*divison ; i < (fps-1)*divison + ((fps == divison) ? remainder : sliced) ; i++){
		            	TableContents tc = tList.get(i);
		            	String read = tc.getReading() ? "읽음" : "읽지않음"; 
						model.addRow(new Object[]{read,tc.getSender(),tc.getSenderURL(),tc.getSubject(),tc.getTime()});
					}
		        }
			});
			
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		}
		add(jSlider,BorderLayout.SOUTH);
		add(jScrollPane,BorderLayout.NORTH);
	}
}


class TableContents{	
	private boolean reading;
	private String folderName;
	private String subject;
	private String sender;
	private String senderURL;
	private String time;
	
	public void setReading(boolean reading){
		this.reading = reading;
	}
	public void setFolderName(String folderName){
		this.folderName = folderName;
	}
	public void setSubject(String subject){
		this.subject = subject;
	}
	public void setSender(String sender){
		this.sender = sender;
	}
	public void setSenderURL(String senderURL){
		this.senderURL = senderURL;
	}
	public void setTime(String time){
		this.time = time;
	}
	public boolean getReading(){
		return this.reading;
	}
	public String getFolderName(){
		return this.folderName;
	}
	public String getSubject(){
		return this.subject;
	}
	public String getSender(){
		return this.sender;
	}
	public String getSenderURL(){
		return this.senderURL;
	}
	public String getTime(){
		return this.time;
	}
}