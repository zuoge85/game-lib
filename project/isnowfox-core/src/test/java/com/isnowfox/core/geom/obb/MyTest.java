package com.isnowfox.core.geom.obb;

import com.isnowfox.core.geom.Rectangle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class MyTest extends JPanel {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("AffineTransform");
                frame.setSize(1000, 600);
//				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                Container contentPane = frame.getContentPane();
                contentPane.add(new MyTest());
                frame.setVisible(true);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
            }
        });

    }

    public MyTest() {
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint();
            }
        };
        new Timer(0, taskPerformer).start();
    }

    public void paint() {
        innerPaint();

        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paint();
            }
        };
        new Timer(100, taskPerformer).start();
    }

    private double r = 1;

    public void innerPaint() {
        OBBRectangle oobRect1 = new OBBRectangle();
        oobRect1.setByLeftTop(270, 200, 50, 200, Math.toRadians(r));

        OBBRectangle obbRect0 = new OBBRectangle();
        obbRect0.setByLeftTop(200, 200, 50, 200, 0);

        int color = OBBUtils.detector(obbRect0, oobRect1) ? 0xff0000 : 0xff00;


        Image bufferImg = this.createImage(getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) bufferImg.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.clearRect(0, 0, getWidth(), getHeight());


        paintRect(g2, obbRect0, color);

        paintRect(g2, oobRect1, color);

        // System.out.println(OBBUtils.detector(obbRect0, oobRect1));
        r += 0.05;

        getGraphics().drawImage(bufferImg, 0, 0, null);
    }

    public void paintRect(Graphics2D g2, OBBRectangle obbRect, int color) {
        AffineTransform at = new AffineTransform();
        OBBVector centerPoint = obbRect.getCenterPoint();
        at.rotate(obbRect.getRotation(), centerPoint.getX(), centerPoint.getY());
        at.translate(obbRect.getX(), obbRect.getY());
        g2.drawRenderedImage(new MyBufferedImage(obbRect, color), at);

        Rectangle rect = obbRect.getBounds();

        g2.drawRect((int) Math.round(rect.getX()), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
    }

    public static class MyBufferedImage extends BufferedImage {
        public MyBufferedImage(OBBRectangle oobRect, int color) {
            this(oobRect, oobRect.getBounds(), color);
        }

        public MyBufferedImage(OBBRectangle obbRect, Rectangle rect0, int color) {
            super((int) obbRect.getWidth() + 1, (int) obbRect.getHeight() + 1, (int) BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2 = createGraphics();
//			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(color));
            g2.drawRect(0, 0, (int) obbRect.getWidth(), (int) obbRect.getHeight());

            g2.setColor(new Color(color));
            int x1 = (int) obbRect.getWidth() / 2;
            int y1 = (int) obbRect.getHeight() / 2;
            g2.drawLine(x1, y1, x1, y1);
        }
    }
}