package zeitrechnung;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

/**
 * Dies ist die Hauptklasse des Dabendorfer Zeitrechners. Er rechnet die Dabendorfer Zeit in Zeiten des gregorianischer Kalenders um und andersherum.
 * 
 * @author Lukas Schramm
 * @version 1.0
 *
 */
public class Zeitrechner {
	
	private JFrame frame1 = new JFrame("Zeitrechner");
	private NumberFormat format1 = NumberFormat.getInstance(); 
	private NumberFormatter formatter1 = new NumberFormatter(format1);
	private JFormattedTextField gregKalenderTF;
	private JFormattedTextField dorZeitTF = new JFormattedTextField(formatter1);
	private JRadioButton gregRadio = new JRadioButton("Greg.-Zeit");
    private JRadioButton dorRadio = new JRadioButton("DOR-Zeit");
    private ButtonGroup bg = new ButtonGroup();
    private ArrayList<JFormattedTextField> textfelder = new ArrayList<JFormattedTextField>();
    private DabendorferZeit dorZeit;
	
	public Zeitrechner() {
		dorZeit = new DabendorferZeit();
		gregKalenderTF = new JFormattedTextField(dorZeit.getDateFormat());
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setPreferredSize(new Dimension(260,80));
		frame1.setMinimumSize(new Dimension(260,80));
		frame1.setMaximumSize(new Dimension(390,120));
		frame1.setResizable(true);
		Container cp = frame1.getContentPane();
		cp.setLayout(new GridLayout(2,1));
		
		JPanel panelGreg = new JPanel();
		panelGreg.setLayout(new BorderLayout());
		panelGreg.add(gregRadio,BorderLayout.LINE_START);
		panelGreg.add(gregKalenderTF,BorderLayout.CENTER);
		JPanel panelDOR = new JPanel();
		panelDOR.setLayout(new BorderLayout());
		panelDOR.add(dorRadio,BorderLayout.LINE_START);
		panelDOR.add(dorZeitTF,BorderLayout.CENTER);
		cp.add(panelGreg);
		cp.add(panelDOR);
		textfelder.add(gregKalenderTF);
		textfelder.add(dorZeitTF);
		
		bg.add(gregRadio);
	    bg.add(dorRadio);
		gregRadio.setSelected(true);
		gregKalenderTF.setText("06.05.1821 03:14:15");
		dorZeitTF.setText("0");
		for(JFormattedTextField jftf:textfelder) {
			jftf.getDocument().addDocumentListener(new DocumentListener() {
	            @Override
	            public void changedUpdate(DocumentEvent e) {
	            }
	            @Override
	            public void insertUpdate(DocumentEvent e) {
	                SwingUtilities.invokeLater(new Runnable() {
	                    @Override
	                    public void run() {
	                    	umrechnen();
	                    }
	                });
	            }
	            @Override
	            public void removeUpdate(DocumentEvent e) {
	            }
	        });
		}
		format1.setGroupingUsed(false);
	    formatter1.setAllowsInvalid(false);
		frame1.pack();
		frame1.setLocationRelativeTo(null);
		frame1.setVisible(true);
	}
	
	/**
	 * Diese Methode prueft, welcher Radiobutton als Ursprungszeit ausgewaehlt wurde.
	 * Anschliessend liest sie den Wert ein, rechnet die Zeit um und fuegt den errechneten Wert in das Textfeld ein.
	 */
	private void umrechnen() {
        if(gregRadio.isSelected()) {
        	dorZeit.gregZuDOR(gregKalenderTF.getText());
        	long neu = dorZeit.getDorZeit();
        	if(String.valueOf(neu)!=dorZeitTF.getText()) {
        		dorZeitTF.setText(String.valueOf(neu));
        	}
        } else {
        	try {
	        	dorZeit.dorZuGreg(Long.valueOf(dorZeitTF.getText()));
	        	if(dorZeit.getGregKalender().getTime()!=dorZeit.getDateFormat().parse(gregKalenderTF.getText())) {
	        		gregKalenderTF.setValue(dorZeit.getGregKalender().getTime());
	        	}
        	} catch (ParseException e) {
        		JOptionPane.showMessageDialog(null, "Du hast falsche Werte eingetragen."+System.getProperty("line.separator")+"Wenn Du dies nicht korrigierst"+System.getProperty("line.separator")+"bekommst Du kein Ergebnis!", "Falscheingabe", JOptionPane.WARNING_MESSAGE);
        	}
        }
    }
	
	public static void main(String[] args) {
		new Zeitrechner();
	}
}