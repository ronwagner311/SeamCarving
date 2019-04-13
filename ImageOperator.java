import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageOperator {
	BufferedImage img=null;
	public	ImageOperator(String s){
		this.img= getImage(s);
	}
	public int[][] GetMatrix(){
		System.out.println(this.img.getWidth()+" "+this.img.getHeight());
		int [][] matrix= new int [this.img.getHeight()][this.img.getWidth()];
		for(int y=0; y<this.img.getHeight();y++) {
			for(int x=0; x<this.img.getWidth();x++) {
				matrix[y][x]=this.img.getRGB(x,y);
			}
		}
		System.out.println("matrix returned");
		return matrix;
	}
	public void testimg() throws IOException {
		int color= 128;
		int green=0;
		int red =0;
		BufferedImage out= new BufferedImage(100,100,1);
		for(int l=0;l<100;l++) {
			for(int k=0;k<100;k++) {
				out.setRGB(l, k,(red<<24)|(green<<16)|(color<<8)|color);
			}
		}
	File output= new File("RemoveSeam.jpg");
	ImageIO.write(out,"jpg",output);
	}
	public static void copy(int [][] matrix_og) throws IOException {
		BufferedImage out= new BufferedImage(matrix_og[0].length,matrix_og.length,1);
		int [] p=null;
		for(int i=0;i<matrix_og.length;i++) {
			p=matrix_og[i];
			for(int j=0;j<p.length;j++) {
				out.setRGB(j, i,p[j]);
			}
		}
	File output= new File("RemoveSeam.jpg");
	ImageIO.write(out,"jpg",output);
	}
	public BufferedImage getImage(String s) {
	
		try {
			System.out.println("file opened");
			return ImageIO.read(new File(s));
		} catch (IOException e) {
			System.out.println("file not found");
		}		
	return img;
	}
}
