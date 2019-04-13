import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
/*this file contains the following functions :-
RemoveSeam: removes the seam given as the first argument, seam[0] will decide whether it is a vertical
of a horizontal seam, see the comment at the start of the function
AddsSeam: basically the same as RemoveSeam.
concat: concatenate two arrays.
greyScale: returns a greyScaled Image.
transpose: transposes the given matrix
*/
public class ImageManipulation {
	
	public static int[][] RemoveSeam2(int[][] image, int[] seam){
		int[][] image2 = new int[image.length][image[0].length-1];
		int [] p=null;
		int [] start=null;
		int [] end=null;
		for(int i=0;i<image.length;i++) {
			start=Arrays.copyOfRange(image[i],0,Math.min(seam[i],image[i].length));
			end=Arrays.copyOfRange(image[i],seam[i]+1, image[i].length);
			p=concat(start,end);
			image2[i]=p;
		}
		return image2;
	}
	
	public static void RemoveSeam(int[] seam,int [][] matrix,Operators line) throws Exception {// seam[0]=0 horizontal,
		BufferedImage out=null;
		String s="RemoveSeam.jpg";
		int loop = 0;
		if(seam.length!=matrix[0].length&&seam.length!=matrix.length) {
			System.out.println("check seem length");
		}
		if(line==Operators.column) {
			System.out.println("removing column");
			matrix=transpose(matrix);
			out= new BufferedImage(matrix[0].length-1,matrix.length,1);
			loop=out.getHeight();
		}
		else {
			System.out.println("removing row");
			out= new BufferedImage(matrix.length,matrix[0].length-1,1);
			loop= out.getWidth();
		}
		int [] p=null;
		int [] start=null;
		int [] end=null;
		for(int i=0;i<loop;i++) {
			start=Arrays.copyOfRange(matrix[i],0,Math.min(seam[i+1],matrix[i].length));
			end=Arrays.copyOfRange(matrix[i],seam[i+1]+1, matrix[i].length);
			p=concat(start,end);
			for(int j=0;j<p.length;j++) {
				if(loop==out.getHeight())
					out.setRGB(j,i,p[j]);
				else
					out.setRGB(i, j, p[j]);
			}
		}
		File output= new File(s);
		ImageIO.write(out,"jpg",output);
		return ;
	}
	
	public static void DrawSeam(int[] seam,int [][] matrix,Operators line) throws Exception {// seam[0]=0 horizontal,
		String s="AddSeam.jpg";
		BufferedImage out=null;
		int loop = 0;
		if(seam.length!=matrix[0].length&&seam.length!=matrix.length) {
			System.out.println("check seem length");
		}
		if(line==Operators.column) {
			System.out.println("adding column");
			matrix=transpose(matrix);
			out= new BufferedImage(matrix[0].length+1,matrix.length,1);
			loop=out.getHeight();
		}
		else {
			System.out.println("adding row");
			out= new BufferedImage(matrix.length,matrix[0].length+1,1);
			loop= out.getWidth();
		}
		int [] p=null;
		int [] start=null;
		int [] end=null;
		System.out.println(loop);
		for(int i=0;i<loop;i++) {
			int [] calculatedValue= {seam[i+1]};
			start=Arrays.copyOfRange(matrix[i],0,Math.min(seam[i+1],matrix[i].length));
			end=Arrays.copyOfRange(matrix[i],seam[i+1]+1, matrix[i].length);
			p=concat(start,calculatedValue);
			p=concat(p,end);
			for(int j=0;j<p.length;j++) {
				if(loop==out.getHeight())
					out.setRGB(j,i,p[j]);
				else
					out.setRGB(i, j, p[j]);
			}
		}
		File output= new File(s);
		ImageIO.write(out,"jpg",output);
		return ;
	}



	public static void main(String [] args) throws Exception {
		ImageOperator x = new ImageOperator("strawberry.jpg");
	
		//GreyScale(x);
	}
	
	
	
	
	public static  int[] concat(int [] arr1,int [] arr2) {
		int [] x= new int [arr1.length+arr2.length];
		for(int i =0;i<arr1.length;i++) {
			x[i]=arr1[i];		
		}
		for(int i=arr1.length;i<x.length;i++) {
			x[i]=arr2[i-arr1.length];
		}
		return x;
	}
		
	public static int[][] transpose(int[][] matrix){
		int [] [] mat_o=new int [matrix[0].length][matrix.length];
		for(int i=0;i<matrix.length;i++) {
			for(int j=0;j<matrix[0].length;j++) {
				mat_o[j][i]=matrix[i][j];
			}
		}
		return mat_o;
	}
	
	/*public static void GreyScale(ImageOperator op) {
		BufferedImage out= new BufferedImage(op.img.getWidth(),op.img.getHeight(),1);
		int [][] mat =op.GetMatrix();
		int blue,green,red,color=0;
		for(int i=0;i<op.img.getWidth();i++) {
			for(int j=0;j<op.img.getHeight();j++) {
				blue=(mat[i][j]>>8)&0xff;
				green=(mat[i][j]>>16)&0xff;
				red=(mat[i][j]>>24)&0xff;
				color=(int) (0.3*red+0.3*green+0.3*blue);
				color=(color<<24)|color<<16|color<<8|(color&0xff);
				out.setRGB(i, j, color);		
			}
		}
		File output= new File("greyScale.jpg");
		try {
			ImageIO.write(out,"jpg",output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ;
	}*/
}

	