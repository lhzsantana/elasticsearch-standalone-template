package templates_elasticsearch.standalone;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.elasticsearch.ElasticsearchException;

import elasticsearch_file.not_call.NotCall_Reader;
import elasticsearch_file.sender.businessfax.BusinessFax_Reader;
import elasticsearch_file.sender.city.City_Reader;
import elasticsearch_file.sender.landlinewireless.LandlineWireless_Reader;

public class App {

	private static JComboBox<String> fileList;
	private static JFileChooser saveFile;
	private static JLabel feedbackLabel;

	public static void main(String[] args) {
		JFrame frame = new JFrame("File sender App");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.add(panel);
		placeComponents(panel);

		frame.setVisible(true);
	}

	private static void placeComponents(JPanel panel) {

		panel.setLayout(null);

		JLabel userLabel = new JLabel("Email");
		userLabel.setBounds(10, 10, 80, 25);
		panel.add(userLabel);

		final JTextField userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		panel.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		passwordLabel.setBounds(10, 40, 80, 25);
		panel.add(passwordLabel);

		final JPasswordField passwordText = new JPasswordField(20);
		passwordText.setBounds(100, 40, 160, 25);
		panel.add(passwordText);

		feedbackLabel = new JLabel("");
		feedbackLabel.setBounds(10, 140, 160, 60);
		panel.add(feedbackLabel);
		
		JButton sendButton = new JButton("Send");
		sendButton.setBounds(180, 140, 80, 25);
		panel.add(sendButton);
		
		sendButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				Login login= new Login();
				if(login.doLogin(userText.getText(), passwordText.getText())){
						
					feedbackLabel.setText("Starting upload");				
					File targetFile = saveFile.getSelectedFile();
					if(targetFile!=null){
	

						if (fileList.getSelectedItem().equals("Not call")) {
							NotCall_Reader reader = new NotCall_Reader();
							try {
								reader.read(targetFile);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else if (fileList.getSelectedItem().equals("Business-Fax")) {
							BusinessFax_Reader reader = null;
							try {
								if(targetFile.isDirectory()){
									
									reader = new BusinessFax_Reader();										
									reader.read(targetFile.getAbsolutePath());
								}else{
									feedbackLabel.setText("This is not a directory");						
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (fileList.getSelectedItem().equals(
								"Landline-Wireless")) {
							LandlineWireless_Reader reader = null;
							try {
								reader = new LandlineWireless_Reader();
							} catch (ElasticsearchException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								reader.read(targetFile);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (fileList.getSelectedItem().equals("City")) {
		
							City_Reader reader = null;
							try {
								reader = new City_Reader();
							} catch (ElasticsearchException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							try {
								reader.read(targetFile);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						feedbackLabel.setText("Finished upload");
					
					}
					else{
						feedbackLabel.setText("Choose a file");
					}
				}else{
					feedbackLabel.setText("Wrong login");
				}
			}
		});

		JLabel fileLabel = new JLabel("File");
		fileLabel.setBounds(10, 70, 80, 25);
		panel.add(fileLabel);

		JButton chooseFileButton = new JButton("Choose file...");
		chooseFileButton.setBounds(100, 70, 80, 25);
		panel.add(chooseFileButton);

		String strFile = "";
		saveFile = new JFileChooser();
		saveFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		chooseFileButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				saveFile.showSaveDialog(null);
			}
		});

		/*
		 * 
		 * JFileChooser fc = new JFileChooser(); fc.setBounds(10, 70, 80, 25);
		 * fc.showOpenDialog(panel); //fc.showOpenDialog(this);
		 * 
		 * fc.getSelectedFile();
		 */
		JLabel typeLabel = new JLabel("Type");
		typeLabel.setBounds(10, 100, 80, 25);
		panel.add(typeLabel);

		String[] fileStrings = { "Not call", "Business-Fax", "Landline-Wireless", "City" };
		
		// Create the combo box, select item at index 4.
		// Indices start at 0, so 4 specifies the pig.
		fileList = new JComboBox(fileStrings);
		// fileList.setSelectedIndex(0);
		// fileList.addActionListener(this);
		fileList.setBounds(100, 100, 160, 25);

		panel.add(fileList);
	}
	
	public static void updateMessage(String message){
		feedbackLabel.setText(message);
	}
}
