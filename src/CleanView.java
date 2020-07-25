import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;

import javax.swing.*;

import files.FileUtils;
import files.TextFileHandler;



/**
 * Bug: does not work correctly if placed in a directory with spaces in the filename.
 *  
 * @author CheeseyBob
 */
class CleanView extends JFrame implements ActionListener, KeyListener {
	private static final long serialVersionUID = 1L;
	private static final String TITLE = "CleanView";
	private static final String VERSION = "1.0";
	
	private JLabel label = new JLabel("Paste video URL below:");
	private JTextField textField = new JTextField();
	private JButton button = new JButton("Play");
	private String htmlFilename = "webpage.html";
	private boolean clearInputAfterOpeningVideo = true;
	private String backgroundColor = "#181818";
	
	static String getYouTubeVideoCode(String videoURL) {
		int lastEqualsIndex = videoURL.lastIndexOf("="); // For normal video URLs.
		int lastSlashIndex = videoURL.lastIndexOf("/"); // For copy url right-click option.
		int cutIndex = Math.max(lastSlashIndex, lastEqualsIndex) + 1;
		return videoURL.substring(cutIndex);
	}
	
	static String getYouTubeEmbedCode(String videoCode) {
		String embedCode = "<iframe ";
		embedCode += "width=\"1908\" ";
		embedCode += "height=\"759\" ";
		embedCode += "src=\"https://www.youtube.com/embed/"+videoCode+"\" ";
		embedCode += "frameborder=\"0\" ";
		embedCode += "allow=\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\" ";
		embedCode += "allowfullscreen>";
		embedCode += "</iframe>";
		return embedCode;
	}

	public static void main(String[] args) {
		CleanView program = new CleanView();
		FileUtils.setExecutionPath(program);
		program.setVisible(true);
	}
	
	CleanView(){
		setTitle(TITLE+" v"+VERSION);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(0, 1));
		add(label);
		add(textField);
		textField.setPreferredSize(new Dimension(300, 30));
		add(button);
		button.setPreferredSize(new Dimension(300, 30));
		pack();
		button.addActionListener(this);
		textField.addKeyListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == button) {
			openVideo();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			openVideo();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		;
	}
	private void openHTMLFile() {
		try {
			String path = FileUtils.getExecutionPath(this)+htmlFilename;
			URI uri = new URI("file://"+path);
			Desktop.getDesktop().browse(uri);
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
	}
	
	private void openVideo() {
		// Get the video embed code. //
		String videoCode = getYouTubeVideoCode(textField.getText());
		
		// Write the html file. //
		writeHTMLFile(videoCode);
		
		// Open the html file in a browser. //
		openHTMLFile();
		
		// Clear the input field. //
		if(clearInputAfterOpeningVideo) {
			textField.setText("");
		}
	}
	
	private void writeHTMLFile(String videoCode) {
		LinkedList<String> lineList = new LinkedList<String>();
		lineList.add("<html>");
		lineList.add("<body style=\"background-color:"+backgroundColor+";\">");
		lineList.add(getYouTubeEmbedCode(videoCode));
		lineList.add("</html>");
		TextFileHandler.writeEntireFile(htmlFilename, lineList);
	}
}
