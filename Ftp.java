package rsd_ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

public class Ftp {
static String a;
   public static String user;
   public Socket socket = null, socketData = null;
   public boolean DEBUG = true;
   public String host;
   public int port;
   int fileSize;
	public Socket  dataConnection;
   public BufferedWriter writer;
   //, writerData;
   public BufferedInputStream readerData;
   public BufferedInputStream reader;
   public OutputStream out;
   public String dataIP;
   public int dataPort;
   public  BufferedOutputStream writerData;
static String new_path;
   
   public Ftp(String ipAddress, int pPort, String pUser){
      user = pUser;
      port = pPort;
      host = ipAddress;
   }
   public Ftp(String pUser){
      this("127.0.0.1", 21, pUser);      
   }
   
  public Ftp()
  {
	  
  }
  
  // ---------------------------------------------------------si on connect avec le nom de l'utilisateur 
  
   public String connect(String user) throws IOException{
	   
	   this.user=user;
	   port=21;
	   host="127.0.0.1";
      //Si la socket est déjà initialisée
      if(socket != null)
           a = "La connexion au FTP est déjà activée";
      
      
      //On se connecte
      socket = new Socket(host, port);
      
      //On crée nos objets pour pouvoir communiquer
      reader = new BufferedInputStream(socket.getInputStream());
      writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      
      String response = read();   
      /*
       * Le serveur envoie un code 230 en réponse à une
       *  commande qui a fourni au serveur des informations
       *   d'identification suffisantes pour permettre à 
       *   l'utilisateur d'accéder au serveur FTP.
       * 
       */
      if(response.startsWith("220"))
      {
    	  a= " votre connexion est bien établi";
      }
      else
      {
    	  a= " erreur de connexion ";
    	  
      }

      
      
      send("USER " + user);
      response = read();
      if(!response.startsWith("331"))  
         { a = " Erreur de connexion avec le compte utilisateur : \n" + response;}
      
      //Pas de gestion du mot de passe dans notre cas, mais vous pouvez en préciser un dans le string passwd si vous le souhaitez
      String passwd = "aa";
      send("PASS " + passwd);
      System.out.println("pass = "+passwd);
      response = read();
      if(!response.startsWith("230"))  
         throw new IOException("Erreur de connexion avec le compte utilisateur : \n" + response);

      
      
      
      return a;

      //Nous sommes maintenant connectés
   }
   
   
   // ---------------------------------------------------------Si on connect avec le nom et le mot de passe ( lorque le champ de password est non vide
   
   
   
   
   // ---------------------------------------------------------
   
   public void quit(){
      try {
         send("QUIT");
      } catch (IOException e) {
         e.printStackTrace();
      }  finally{
         if(socket != null){ // 
            try {
               socket.close();
            } catch (IOException e) {
               e.printStackTrace();
            }
            finally{
               socket = null;
            }
         }
      }
   }
   
   //------------------- Méthode permettant d'initialiser le mode passif et ainsi de pouvoir communiquer avec le canal dédié aux données
   // le serveur qui va determiner le port  et ip qu'on va utiliser pour les donnés
   // utiliser dans le cas ou le par-feu filtre le reseau 
   // c'est le mode le plus recomender

   void  enterPassiveMode() throws IOException{

     send("PASV");
     String response = read();
     if(DEBUG)
        log(response);
     String ip = null;
     int port = -1;
     
     //On décortique ici la réponse retournée par le serveur pour récupérer
     //l'adresse IP et le port à utiliser pour le canal data
     int debut = response.indexOf('(');
     int fin = response.indexOf(')', debut + 1);
     if(debut > 0){
        String dataLink = response.substring(debut + 1, fin);
        StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
        try {
           //L'adresse IP est séparée par des virgules
           //on les remplace donc par des points...
           ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
                   + tokenizer.nextToken() + "." + tokenizer.nextToken();
          
           //Le port est un entier de type int
           //mais cet entier est découpé en deux
           //la première partie correspond aux 4 premiers bits de l'octet
           //la deuxième au 4 derniers
           //Il faut donc multiplier le premier nombre par 256 et l'additionner au second
           //pour avoir le numéro de ports défini par le serveur
           port = Integer.parseInt(tokenizer.nextToken()) * 256
                + Integer.parseInt(tokenizer.nextToken());
           dataIP = ip;
           dataPort = port; 
          
        } catch (Exception e) {
          throw new IOException("FTP a reçu des informations de liaison de données incorrectes: "
              + response);
        }        
     }
   }
   
   //-------------------------- Méthode initialisant les flux de communications
 
   void createDataSocket() throws UnknownHostException, IOException{
      socketData = new Socket(dataIP, dataPort);
      readerData = new BufferedInputStream(socketData.getInputStream());
    //  writerData = new BufferedWriter(new OutputStreamWriter(socketData.getOutputStream()));
       writerData = new BufferedOutputStream(socketData.getOutputStream());

   }
   


//------------------------------Permet de changer de répertoire (Change Working Directory)
  
   public synchronized boolean cwd(String dir) throws IOException {
	    send("CWD " + dir);
	    String response = read();
	    return (response.startsWith("250 "));
	  }
   
   
  
	   
   //Affiche les informations d'un fichier ou d'un répertoire spécifique
   
   public String list() throws IOException{
      send("TYPE ASCII");      
      read();
      
      enterPassiveMode();
      createDataSocket();
      send("LIST");
      
      return readData();
   }
   
 // Affiche la liste des noms des fichiers d'un répertoire.
   public String Nlst(String a) throws IOException{
	    send("TYPE ASCII");      
	      read();
	      
	      enterPassiveMode();
	      createDataSocket();
	      send("NLST " + a);
	      
	      return readData();
	   }
	 


 
   //Afficher le répertoire de travail courant
   
   public  String xpwd() throws IOException {
	    send("XPWD");
	    String dir = null;
	    String response = read();
	    if (response.startsWith("257 ")) {
	      int firstQuote = response.indexOf('\"');
	      int secondQuote = response.indexOf('\"', firstQuote + 1);
	      if (secondQuote > 0) {
	        dir = response.substring(firstQuote + 1, secondQuote);
	      }
	    }
	    return dir;
	  }
   
   
   /**
    * Méthode permettant d'envoyer les commandes au FTP
    * @param command
    * @throws IOException
    */
   private void send(String command) throws IOException{
      command += "\r\n";
      if(DEBUG) // debug = true !
         log(command);// >> \r\n + commande
      writer.write(command);    
      writer.flush();
   }
   
   /**
    * Méthode permettant de lire les réponses du FTP
    * @return
    * @throws IOException
    */
   
   private String read() throws IOException{      
      String response = "";
      int stream;
      byte[] b = new byte[4096];//4046 = le nombre des bytes lisés au cours de transfere
      stream = reader.read(b);
      response = new String(b, 0, stream);//b :notre byte : off= 0 pour lire tout le doc  ; len = stream
      
      if(DEBUG)
         log(response);
      return response;
   }
      
   /**
    * Méthode permettant de lire les réponses du FTP
    * @return
    * @throws IOException
    */
   private String readData() throws IOException{
      
      String response = "";
      byte[] b = new byte[1024];
      int stream;
      
      while((stream = readerData.read(b)) != -1){
         response += new String(b, 0, stream);
      }
      
      if(DEBUG)
         log(response);
      return response;
   }
   
   
   public void debugMode(boolean active){
      DEBUG = active;
   }
   
   private void log(String str){
      System.out.println(">> " + str);
   }
   
   
//--------------------------------------- send 
   public synchronized boolean send_doc(File file) throws IOException {
	    if (file.isDirectory()) { // Si le fichier choisie est un repertoire
	    	
	      throw new IOException("SimpleFTP ne peut pas télécharger un répertoire.");
	    }

	    String filename = file.getName(); //  le nom de fichier
         fileSize = (int) file.length(); // la taille de fichier


	    return send_doc(new FileInputStream(file), filename);
	  }

   
   public boolean send_doc(InputStream inputStream, String filename) throws IOException
   {    send("STOR " + filename); // envoyer  la demande STOR avec le nom de fichier
	    BufferedInputStream input = new BufferedInputStream(inputStream);
	 Socket  dataSocket = new Socket(dataIP, dataPort);  // creer une socket pour l'envoie des données
	    BufferedOutputStream output = new BufferedOutputStream(dataSocket.getOutputStream());
	  
	    byte[] buffer = new byte[fileSize];
	    int bytesRead = 0;
	      
	    while ((bytesRead = input.read(buffer)) >0) {

	      output.write(buffer, 0, bytesRead);
	    }
	    output.flush();
	    output.close();
	    input.close();
	    dataSocket.close();
	
   String response = read();
   //Opening data channel for file upload to server of file
    return response.startsWith("150");
	
   }
   
//------------------------------------------retrieve
   
   
   public boolean rec_doc(File file,String a) throws IOException
   {

	   if (file.isDirectory()) {
		      throw new IOException("SimpleFTP ne peut pas télécharger un répertoire.");  // si le fichier choisi est un repertoir 
		    }

		    String filename = file.getName(); // prendre le nom de fichier
	         fileSize  = (int) file.length(); // la taille de fichier

		    return rec_doc(new FileInputStream(file),filename,a);
		    
   
    }
  
  public boolean rec_doc(InputStream inputStream, String filename,String a) throws IOException
		    {		   	   dataConnection = new Socket(dataIP, dataPort); // creer une socker pour le transfer

                BufferedInputStream dataIn = new BufferedInputStream(dataConnection.getInputStream()); 

	
		 	String response;
		 	
		     send("RETR " + filename);
             
		     

		     
		      
		
             byte readIn[] = new byte[fileSize];
             int read;
             int offset = 0;
             // lire le contenue de fichier 
           
        while ((read = dataIn.read(readIn)) > 0) {
            	 
            	 
        	
              offset += read;
                 
             }
           
             
        
             try {
                 File file = new File(a+"\\"+filename); //rier un fichier avec le nom de fichier de server
                 FileOutputStream fos = new FileOutputStream(file);
                 //readIn = les donnes
                 //0 le debut ; offset le nombre des occté a ecrire
                 fos.write(readIn,0,offset); // ecrire le contenu de fichier
                 fos.close();
             } catch (IOException io) {
                 System.out.println("impossible d'ecrire dans le fichier");
             }
		     
		     
		     
		     response = read();
		     return response.startsWith("226");// 226 Transféré avec succès
		 	
		    }
		    
 //-------------------------------------delete
   
   public synchronized String delete_doc(File file) throws IOException {
	    if (file.isDirectory()) {
	      throw new IOException("SimpleFTP cannot upload a directory.");
	    }

	    String filename = file.getName();

	    return delete_doc(filename);
	  }

   public String delete_doc(String pathName) throws IOException
   {

		String response;
		   
		   send("DELE " + pathName);

		
	
	    response = read();
	    return response;
   }
   
   // rename file 
   
   public synchronized boolean renameFile(File file, String Nname) throws IOException {
	    if (file.isDirectory()) {
	      throw new IOException("SimpleFTP cannot upload a directory.");
	    }

	    String filename = file.getName();
	  

	    return renameFile( filename,  Nname);
	  }
   
	  public boolean renameFile(String oldName, String newName)throws IOException
	    {
	        String response ;
		  send("rnfr " + oldName);
	        send ("rnto " + newName);
		    response = read();
	        return response.startsWith("350");
	    }
	 
  //binary mode
  public synchronized boolean bin() throws IOException {
	    send("TYPE I");
	    String response = read();
	    return (response.startsWith("200 "));
	  }

//ascii mode
	  public synchronized boolean ascii() throws IOException {
	    send("TYPE A");
	    String response = read();
	    return (response.startsWith("200 "));
	  }
  
	
}

