package jpeg;

import java.awt.*;
import java.util.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.ImageIO;

public class MakeJPEG extends Canvas implements MouseMotionListener, MouseListener, ActionListener {
	private static final long serialVersionUID = 1L;
	int x = -1, y = -1, 橡皮擦通知 = 0, 清除通知 = 0,叠加通知 = 0;//缩放通知=0
	Vector<Point> v = null;
	int n = 1,k=100;
	int w = 500;
	int h = 500;
	Graphics2D ggg;
	BufferedImage image,im;
	Frame window;
	Dimension r;

	Button 保存, 调色板, 橡皮, 清除, 画笔, 获取屏幕, 打开;
	Color 画笔颜色;
	Scrollbar 滚动条;

	Panel pCenter, pSouth, pNorth, pWest;

	public MakeJPEG() {
		class ScrollBarListener implements AdjustmentListener{

			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				k =e.getValue();
				w=w*k/100;
				h=h*k/100;
				滚动条.setValue(100);
				叠加通知 = 1;
				image=im;
				repaint();
			}
			
		}

		保存 = new Button("将绘制的图形或屏幕保存为JPG文件");
		获取屏幕 = new Button("获取屏幕");
		打开 = new Button("打开JPG格式");
		调色板 = new Button("打开调色板");
		画笔 = new Button("画笔");
		橡皮 = new Button("橡皮");
		清除 = new Button("清除");
		滚动条=new Scrollbar(Scrollbar.VERTICAL,100,1,50,150);
		滚动条.setPreferredSize(r=new Dimension(10,h));
		滚动条.setBackground(Color.gray);
		

		调色板.addActionListener(this);
		打开.addActionListener(this);
		保存.addActionListener(this);
		画笔.addActionListener(this);
		橡皮.addActionListener(this);
		清除.addActionListener(this);
		滚动条.addAdjustmentListener(new ScrollBarListener());
		获取屏幕.addActionListener(this);
		画笔颜色 = new Color(0, 0, 0);
		addMouseMotionListener(this);
		addMouseListener(this);

		v = new Vector<Point>();
		Color c = new Color(1f,1f,1f,1f);
		this.setBackground(c);
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		ggg = image.createGraphics();
		Rectangle2D rect = new Rectangle2D.Double(0, 0, w, h);
		ggg.setColor(getBackground());
		ggg.fill(rect);

		window = new Frame("JPEG图像生成器");
		pCenter = new Panel();
		pCenter.setLayout(null);
		pCenter.add(this);
		pCenter.setBackground(Color.gray);
		this.setBounds(0, 0, w, h);
		window.add(pCenter, BorderLayout.CENTER);
		pNorth = new Panel();
		pNorth.add(保存);
		pNorth.add(打开);
		pNorth.add(获取屏幕);
		window.add(pNorth, BorderLayout.NORTH);

		pSouth = new Panel();
		pSouth.add(调色板);
		pSouth.add(橡皮);
		pSouth.add(清除);
		pSouth.add(画笔);
		window.add(pSouth, BorderLayout.SOUTH);
		
		pWest = new Panel();
		pWest.add(滚动条);
		window.add(pWest, BorderLayout.WEST);
		window.setVisible(true);

		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		window.setBounds(0, 0, w+40, h+120);
		window.validate();
	}

	public void paint(Graphics g) {

		if (x != -1 && y != -1 && 橡皮擦通知 == 0 && 清除通知 == 0) {
			g.setColor(画笔颜色);
			n = v.size();

			for (int i = 0; i < n - 1; i++) {
				Point p1 = (Point) v.elementAt(i);
				Point p2 = (Point) v.elementAt(i + 1);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
				ggg.setColor(g.getColor());
				ggg.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		} else if (橡皮擦通知 == 1 && 清除通知 == 0) {
			g.setColor(getBackground());
			g.fillRect(x, y, 4, 4);
			ggg.setColor(getBackground());
			ggg.fillRect(x, y, 4, 4);
		} else if (清除通知 == 1 && 橡皮擦通知 == 0) {
			g.setColor(getBackground());
			g.fillRect(0, 0, w, h);
			ggg.setColor(getBackground());
			ggg.fillRect(0, 0, w, h);
			清除通知 = 0;
		} 
		/*if (缩放通知 == 1) {
			缩放通知 = 0;
			//g.drawImage(image,0,0,w,h,this);
			//ggg.drawImage(image, 0,0, w, h,this);
			//this.setBounds(0, 0, w, h);
			//this.repaint();
			叠加通知=1;
		}*/
		if (叠加通知 == 1) {
			//ggg.drawImage(im,0,0,w,h,this);
			//this.setBounds(0, 0, w, h);
			image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
			ggg = image.createGraphics();
			Rectangle2D rect = new Rectangle2D.Double(0, 0, w, h);
			//ggg.setColor(getBackground());
			ggg.fill(rect);
			
			this.setBounds(0, 0, w, h);
			window.setBounds(0, 0, w+40, h+120);
			ggg.drawImage(im,0,0,w,h,this);
			叠加通知=0;
		}
		g.drawImage(image, 0, 0, w, h, this);
		//ggg.drawImage(image, 0,0, w, h,this);
	}

	public void mouseDragged(MouseEvent e) {
		x = (int) e.getX();
		y = (int) e.getY();
		Point p = new Point(x, y);
		v.addElement(p);
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		v.removeAllElements();
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void update(Graphics g) {
		{
			paint(g);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == 橡皮) {
			橡皮擦通知 = 1;
			清除通知 = 0;
		}

		else if (e.getSource() == 清除) {
			橡皮擦通知 = 0;
			清除通知 = 1;
			repaint();
		} else if (e.getSource() == 画笔) {
			橡皮擦通知 = 0;
			清除通知 = 0;
		} else if (e.getSource() == 保存) {
			FileDialog savedialog = new FileDialog(window, "保存图形到JPG格式", FileDialog.SAVE);
			savedialog.setVisible(true);

			if (savedialog.getFile() != null) {
				try {
					String fileName = savedialog.getFile();
					FileOutputStream out = new FileOutputStream(fileName);
					ImageIO.write(image,"jpg", out);
					out.close();
				} catch (Exception EE) {
				}
			}
		} else if (e.getSource() == 获取屏幕) {
			Robot robot = null;
			try {
				robot = new Robot();
			} catch (Exception er) {
			}

			Rectangle screenRect = null;
			int width = getToolkit().getScreenSize().width;
			int height = getToolkit().getScreenSize().height;
			screenRect = new Rectangle(0, 0, width, height);
			w = width;
			h = height;

			window.setVisible(false);
			this.window.setVisible(false);
			im = robot.createScreenCapture(screenRect);
			叠加通知=1;
			window.setVisible(true);
			this.window.setVisible(true);
			repaint();
		} else if (e.getSource() == 调色板) {
			Color tempColor = JColorChooser.showDialog(window, "调色板", 画笔颜色);
			{
				if (tempColor != null) {
					画笔颜色 = tempColor;
					画笔.setForeground(画笔颜色);

				}
			}
		} else if (e.getSource() == 打开) {

			FileDialog loaddialog = new FileDialog(window, "打开JPG格式", FileDialog.LOAD);
			loaddialog.setVisible(true);
			if (loaddialog.getFile() != null) {

				try {
					String fileN = loaddialog.getDirectory() + loaddialog.getFile();
					FileInputStream input = new FileInputStream(fileN);
					im=ImageIO.read(input);
					w = im.getWidth();
					h = im.getHeight();
					叠加通知=1;
					repaint();
				} catch (Exception ee) {

				}
			}
		}

	}
	public static void main(String[] args) {

		new MakeJPEG();
	}
}
