package rsd_ftp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.JToolBar;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JList;
import javax.swing.JTextArea;
import java.awt.Checkbox;

public class Fenetre__ {

	private JFrame frame;
	private JTextField usertext;
	private JLabel psid;
	private JPasswordField passText;
	private JLabel lblChooseFile;
	private JTextField filePath;
	JLabel modeTxt ;
	String chemin;
	   Ftp ftp = new Ftp();
	   String Newligne=System.getProperty("line.separator");
	private JButton btnQuit;
	
	public String user;
	private Checkbox checkdelete;
	private Checkbox checkliste;
	private JTextField putin;
	private JLabel putint;
	private JLabel lblWhatYouWant;
	private Checkbox checkrename , checkput ,checkget;
	private JButton selectdir;
	private JButton done;
	int d;
	String directory;
	String where;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Fenetre__ window = new Fenetre__();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Fenetre__() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBackground(Color.black);
		frame.getContentPane().setBackground(new Color(255, 222, 179));
	       Toolkit kit = Toolkit.getDefaultToolkit();
	       // Modifier l'icône de JFrame
	       Image img_back = kit.getImage("C:\\Users\\toshiba\\Desktop\\icon.jpg");

	       Image img = kit.getImage("C:\\Users\\toshiba\\Desktop\\icon.jpg");
	       frame.setIconImage(img);
	 
	        // Modifier le titre de la fenêtre
	       frame.setTitle("FTP CLIENT");
	       
	//	frame.setIconImage(new ImageIcon("a.gif"));
		frame.setBounds(100, 100,817, 500);
		frame.setFont(new Font("Tempus Sans ITC", Font.BOLD | Font.ITALIC, 20));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea text_r = new JTextArea();
		text_r.setBackground(new Color(255, 248, 220));
		text_r.setBounds(24, 265, 593, 186);
		frame.getContentPane().add(text_r);
		
		
		JLabel userid = new JLabel("User:");
		//userid.setForeground(Color.WHITE);
		userid.setBackground(Color.BLACK);
		userid.setFont(new Font("Tempus Sans ITC", Font.BOLD | Font.ITALIC, 20));
		userid.setBounds(0, 4, 80, 30);
		frame.getContentPane().add(userid);
		
		usertext = new JTextField();
		usertext.setBackground(new Color(255, 248, 220));
		usertext.setBounds(64, 4, 181, 30);
		frame.getContentPane().add(usertext);
		usertext.setColumns(10);
		
		psid = new JLabel("PASSWORD:");
		psid.setForeground(Color.black);
		psid.setFont(new Font("Tempus Sans ITC", Font.BOLD | Font.ITALIC, 20));
		psid.setBounds(255, 4, 133, 30);
		frame.getContentPane().add(psid);
		
		passText = new JPasswordField();
		passText.setBackground(new Color(255, 248, 220));
		  
		passText.setBounds(382, 4, 181, 28);
		frame.getContentPane().add(passText);
		passText.setColumns(10);
		
		lblChooseFile = new JLabel("CHOOSE FILE:");
		lblChooseFile.setBackground(Color.BLACK);
		//lblChooseFile.setForeground(Color.WHITE);
		lblChooseFile.setFont(new Font("Tempus Sans ITC", Font.BOLD | Font.ITALIC, 20));
		lblChooseFile.setBounds(10, 143, 152, 30);
		frame.getContentPane().add(lblChooseFile);
		
		filePath = new JTextField();
		filePath.setBackground(new Color(255, 248, 220));
		filePath.setBounds(172, 146, 276, 30);
		filePath.setEditable(false);
		frame.getContentPane().add(filePath);
		filePath.setColumns(10);
		//--------------------------------------- choose file
		JButton btnSelect = new JButton("SELECT");
		btnSelect.setFont(new Font("Andalus", Font.BOLD, 18));
		btnSelect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
				JFileChooser fileChooser = new JFileChooser(directory);	// pour le choix des fichiers			
				fileChooser.showOpenDialog(btnSelect);
				chemin=fileChooser.getSelectedFile().getAbsolutePath(); // prendre Path de fichier selection
				filePath.setText(chemin);
			}
		});
		
		btnSelect.setBounds(521, 141, 102, 36);
		frame.getContentPane().add(btnSelect);
	// --------------------------------------------connecte button	
		JButton connecter = new JButton("LOG IN");
		connecter.setFont(new Font("Andalus", Font.BOLD, 18));
		connecter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 user =usertext.getText();
				 
		      
		         try {
		        text_r.setText(ftp.connect(user) + Newligne);	 
					
					 ftp.debugMode(true);
			         ftp.enterPassiveMode();
			        
				       modeTxt.setText("Online");
					   modeTxt.setForeground(Color.green); 
						

				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println(ftp.a);
				}
		    
				
			}
		});
		
		connecter.setBounds(573, 2, 102, 36);
		frame.getContentPane().add(connecter);
		
		modeTxt = new JLabel("offline");
		modeTxt.setFont(new Font("Wide Latin", Font.PLAIN, 17));
		modeTxt.setForeground(Color.RED);
		modeTxt.setBounds(690, 3, 111, 36);
		frame.getContentPane().add(modeTxt);
		
		btnQuit = new JButton("QUIT");
		btnQuit.setFont(new Font("Andalus", Font.BOLD, 18));
		btnQuit.setForeground(Color.BLACK);
		btnQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				   ftp.quit();
				 modeTxt.setText("offline");
					modeTxt.setForeground(Color.red); 
					passText.setText("");
					usertext.setText("");
					text_r.setText("");
					filePath.setText("");
					checkdelete.setState(false);
					checkliste.setState(false);
					checkrename.setState(false);
					checkget.setState(false);
					checkput.setState(false);
					putin.setVisible(false);
					putint.setVisible(false);
					selectdir.setVisible(false);
					done.setVisible(false);
				
			}
		});
		btnQuit.setBounds(690, 415, 102, 36);
		frame.getContentPane().add(btnQuit);
		
		
		
		//frame.getContentPane().add(connecter, new ImageIcon("C:\\Users\\toshiba\\Desktop\\ftp-client.jpg"), d);
		
		 checkput = new Checkbox("PUT");
		checkput.setFont(new Font("Andalus", Font.BOLD | Font.ITALIC, 16));
		checkput.setBounds(221, 53, 95, 22);
		frame.getContentPane().add(checkput);
		
		 checkget = new Checkbox("GET");
		checkget.setFont(new Font("Andalus", Font.BOLD | Font.ITALIC, 18));
		checkget.setBounds(353, 53, 95, 22);
		frame.getContentPane().add(checkget);
		
		checkdelete = new Checkbox("DELETE");
		checkdelete.setFont(new Font("Andalus", Font.BOLD | Font.ITALIC, 18));
		checkdelete.setBounds(221, 81, 95, 22);
		frame.getContentPane().add(checkdelete);
		
		checkliste = new Checkbox("LIST");
		checkliste.setFont(new Font("Andalus", Font.BOLD | Font.ITALIC, 18));
		checkliste.setBounds(353, 81, 95, 22);
		frame.getContentPane().add(checkliste);
		
		putint = new JLabel("PUT IN:");
		putint.setForeground(Color.WHITE);
		putint.setFont(new Font("Andalus", Font.BOLD | Font.ITALIC, 20));
		putint.setBounds(10, 182, 152, 28);
		putint.setVisible(false);
		frame.getContentPane().add(putint);
		
		putin = new JTextField();
		putin.setBackground(new Color(255, 248, 220));
		putin.setBounds(172, 187, 276, 30);
		putin.setVisible(false);

		frame.getContentPane().add(putin);
		putin.setColumns(10);
		  
		
		lblWhatYouWant = new JLabel("Option");
		lblWhatYouWant.setFont(new Font("Andalus", Font.BOLD, 20));
		lblWhatYouWant.setBounds(10, 61, 209, 30);
		frame.getContentPane().add(lblWhatYouWant);
		
		checkrename = new Checkbox("RENAME\r\n");
		checkrename.setFont(new Font("Andalus", Font.BOLD | Font.ITALIC, 18));
		checkrename.setBounds(221, 109, 116, 22);
		frame.getContentPane().add(checkrename);
		
		selectdir = new JButton("SELECT");
		selectdir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();	// pour le choix des fichiers	
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
				fileChooser.showOpenDialog(selectdir);
				String chemin=fileChooser.getSelectedFile().getAbsolutePath();
				putin.setText(chemin);
				
			}
		});
		selectdir.setBounds(521, 188, 102, 29);
		selectdir.setVisible(false);
		frame.getContentPane().add(selectdir);
		
		done = new JButton("");
		done.setVisible(false);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				if(d==1) // store
				{
		           	 try {
		           		 ftp.quit();
		           		ftp.connect(user);
						 ftp.debugMode(true);
				         ftp.enterPassiveMode();
		                  }
		           	 catch (Exception e1) {}
		           	 
		           	 
					String path =filePath.getText();
					File firstLocalFile = new File(path); 

	        		 
					try {
					Boolean	 done = ftp.send_doc( firstLocalFile );
					
					
				
	           	 if (done) { 
	           		 text_r.setText(text_r.getText() + firstLocalFile.getName() + "   est ajouter au serveur  "+ Newligne); 
	           		 
	            		} 


	           	 
	           	 
					} catch (IOException e1) {
		           		 text_r.setText(text_r.getText() + "Impossible de transformer votre fichier"+ Newligne); 



					} 
		
				}
//-------------------------------------------------------------------------
				if(d==2) // retreive
				{

					String path =filePath.getText();
					String in=putin.getText();

					File firstLocalFile = new File(path);

					try {
		           		 ftp.quit();
		           		ftp.connect(user);
						 ftp.debugMode(true);
				         ftp.enterPassiveMode();
		           	 }
		           	 catch (Exception e1) {
		}
					try {
						ftp.rec_doc(firstLocalFile,in);
						 text_r.setText(text_r.getText() + "bien recuperer"+ Newligne);
						
					
					} catch (IOException e1) {
						 text_r.setText(text_r.getText() + " impossible de recuperer les donnees" + Newligne);
					}
					
				
				
				}
				
				//-------------------------------------------------------------------------

				if(d==3) //delete
				{

					String path =filePath.getText();
					File firstLocalFile = new File(path); 

					
						try {
							String r= ftp.delete_doc( firstLocalFile );
				
					if(r.startsWith("250"))
					{
						 text_r.setText(text_r.getText() + "fichier suprimer  "+ Newligne);
					}
					if(r.startsWith("550"))
					{
						 text_r.setText(text_r.getText() + " fichier non trouver "+ Newligne);
					}
					
					
						

	    
						} catch (IOException e1) {
							 text_r.setText(text_r.getText() + "probleme dans la supression"+ Newligne);
						
						
						
					
				}
				
				}
				//-------------------------------------------------------------------------

				if(d==4)//liste
				{

					
					
					try {
		           		 ftp.quit();
		           		ftp.connect(user);
						 ftp.debugMode(true);
				         ftp.enterPassiveMode();
		           	 }
		           	 catch (Exception e1) {
		}
					
					 try {
						 text_r.setText(ftp.list());
						// text_r.setText(ftp.Nlst(ftp.xpwd()));
						
						
					} catch (IOException e1) {
						// TODO Auto-generated catch block
					}
					
					
								}
				
				//-------------------------------------------------------------------------
if(d==5) // rename
{
	try {
  		 ftp.quit();
  		ftp.connect(user);
		 ftp.debugMode(true);
        ftp.enterPassiveMode();
  	 }
  	 catch (Exception e1) {
}
	
	String path =filePath.getText();
	File firstLocalFile = new File(path); 

	try {
		ftp.renameFile(firstLocalFile, "renamed.txt");
	} catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}
				
				
				
			}
		});
		done.setBounds(656, 159, 124, 36);
		frame.getContentPane().add(done);
		
		JButton btnNewButton = new JButton("Confirmer");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				if(checkput.getState()==true) //store
				{
					/*checkget.setEnabled(false);
					checkdelete.setEnabled(false);
					checkliste.setEnabled(false);*/
					done.setText("Ajouter");
					d=1;
					directory="D:\\";
					   selectdir.setVisible(false);
					   putin.setVisible(false);
					   putint.setVisible(false);

				}
				if(checkget.getState()==true)//retrieve
				{
					done.setText("Récupérer");
					d=2;
					directory="D:\\server";
				   selectdir.setVisible(true);
				   putin.setVisible(true);
				   putint.setVisible(true);

				}
				if(checkdelete.getState()==true)//delete
				{
					done.setText("Suprimer");
					d=3;
					directory="D:\\server";
					   selectdir.setVisible(false);
					   putin.setVisible(false);
					   putint.setVisible(false);

					
				}
				if(checkliste.getState()==true)//lise
				{
					done.setText("liste");	
					d=4;
					directory="D:\\server";
					   selectdir.setVisible(false);
					   putin.setVisible(false);
					   putint.setVisible(false);

				}
				if(checkrename.getState()==true)//rename
				{
					done.setText("Ronommer");	
					d=5;
					directory="D:\\server";
					   selectdir.setVisible(false);
					   putin.setVisible(false);
					   putint.setVisible(false);

				}
			
				done.setVisible(true);
			}
		});
		btnNewButton.setBounds(481, 64, 124, 30);
		frame.getContentPane().add(btnNewButton);
		

	}
}
