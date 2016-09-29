package com.isnowfox.io.serialize.tool;

import com.isnowfox.io.serialize.tool.ui.MainFrame;

import javax.swing.*;

public class SerializeMain {

    public static void start(Config c) {
        MainFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel("ch.randelshofer.quaqua.QuaquaLookAndFeel");
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MainFrame frame = new MainFrame(c);
        frame.setVisible(true);
    }
//	public static void main(String[] args) {
////		MessageAnalyse  analyse = new MessageAnalyse();
////		try (InputStream in = SerializeMain.class.getResourceAsStream("/TestMessage.io")){
////			LineIterator it = IOUtils.lineIterator(in,"utf8");
////			Message m = analyse.analyse(it);
////			System.out.println(m);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		try {
//			//UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (ClassNotFoundException | InstantiationException
//				| IllegalAccessException | UnsupportedLookAndFeelException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		MainFrame frame = new MainFrame();
//		frame.setVisible(true);
//	}
}
