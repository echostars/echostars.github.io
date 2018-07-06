package com.echowork;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.swetake.util.Qrcode;


public class GenerQRcode {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void getQRcode (int version,String myCard,String logoFile,String imagePath) throws Exception {
		// TODO Auto-generated method stub
		Qrcode qrcode = new Qrcode();
		byte[] data = myCard.getBytes("utf-8");
		boolean[][] qrdata = qrcode.calQrcode(data);
		qrcode.setQrcodeErrorCorrect('H');//纠错等级
		qrcode.setQrcodeEncodeMode('B');//B代表其他字符
		
		int size = 100+(version-1)*12;
		qrcode.setQrcodeVersion(version);//版本
		
		BufferedImage bufferedImage = new BufferedImage(size,size,BufferedImage.TYPE_INT_RGB);
		Graphics2D gs = bufferedImage.createGraphics();
		gs.setBackground(Color.WHITE);
		gs.clearRect(0,0,size,size);
		
		int pixoff = 2;
		Random random = new Random();
		int statR=random.nextInt(240);
		int statG=random.nextInt(240);
		int statB=random.nextInt(240);
		int endR=random.nextInt(240);
		int endG=random.nextInt(240);
		int endB=random.nextInt(240);
		
		
		
		for(int i=0;i<qrdata.length;i++){
			for(int j=0;j<qrdata.length;j++){
					
				if(qrdata[i][j]){
					int r = statR+(endR-statR)*(j+1)/qrdata.length;
					int g = statG+(endG-statG)*(i+1)/qrdata.length;
					int b = statB+(endB-statB)*(i+1)/qrdata.length;
						
					Color color = new Color(r,g,b);
					gs.setColor(color);
					gs.fillRect(i*4+pixoff,j*4+pixoff,4,4);
						
				}
			}
		}
		
		BufferedImage logo = scale(logoFile,80,80,true);		
		int imageSize = size/4;
		int eSize = (size-imageSize)/2;
		gs.drawImage(logo,eSize,eSize,imageSize,imageSize,null);
		gs.dispose();
		bufferedImage.flush();
		try{
			ImageIO.write(bufferedImage, "png", new File(imagePath));
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("出现问题！");
		}
		System.out.println("二维码生成！");
	}

	private static BufferedImage scale(String logoFile, int width, int height, boolean hasFiller) throws Exception {
		// TODO Auto-generated method stub
		double ratio=0.0;
		File file=(new File(logoFile));
		BufferedImage srcImage=ImageIO.read(file);
		Image destImage=srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		if((srcImage.getHeight()>height)||(srcImage.getWidth()>width)){
			if(srcImage.getHeight()>srcImage.getWidth()){
				ratio=(new Integer(height).doubleValue()/srcImage.getHeight());
			}
			else{
				ratio=(new Integer(width).doubleValue()/srcImage.getWidth());
				
			}
			AffineTransformOp op=new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio),null);
			destImage=op.filter(srcImage, null);
		}
		if(hasFiller){
			BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic=image.createGraphics();
			graphic.setColor(Color.white);
			
			graphic.fillRect(0, 0, width, height);
			if(width==destImage.getWidth(null)){
				graphic.drawImage(destImage, 0, (height-destImage.getHeight(null))/2, destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
			}
			else {
				graphic.drawImage(destImage,  (width-destImage.getWidth(null))/2,0, destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
			}
			graphic.dispose();
			destImage=image;
		}
		
		return (BufferedImage) destImage;
		
	}
	public static void main(String[] args) throws Exception{
		String myCard= "BEGIN:VCARD\n"+
		"N:赵\n"+
		"FN:**\n"+
		"ORG:河北科技师范学院\n"+
		"TITLE:学生\n"+
		"ADR;WORK:科师\n"+
		"ADR;HOME:河北\n"+
		"TEL;WORK,VOICE:*\n"+
		"URL;WORK;:HTTP://WWW.BAIDU.COM\n"+
		"EMAIL;Internet,HOME：1027544008@qq.com\n"+
		"END:VCARD";
		int version=12;
		String logoFile="D:/baidu.jpg";
		String imagePath="D:/baiducode.png";
		GenerQRcode.getQRcode(version, myCard, logoFile, imagePath);
	}
	

}
