import  javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import java.util.Arrays;

public class SeamCarving {

	public static void main(String[] args) throws IOException {
		ImageOperator image = new ImageOperator("test1.jpg");
		System.out.println("image hight: " + Integer.toString(image.img.getHeight()) + ",image width: " + Integer.toString(image.img.getWidth()));
		GreyScale(image);
		int[][] mat = new int[4][4];
		for(int i = 0; i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				mat[i][j]=4*i+j+1;
			}
		}
		for(int i = 0; i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				System.out.print(mat[i][j] + "\t");
			}
			System.out.println("");
		}
		double[][] mat2 = new double[4][4];
		for(int i = 0; i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				mat2[i][j]=EnergyFunction(mat,0,i,j);
			}
		}
		System.out.println("");
		for(int i = 0; i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				System.out.print(String.format("%2.5f", mat2[i][j])  + "    ");
			}
			System.out.println("");
		}
		double[][] mat3 = dynamicPrograming(mat,0);
		System.out.println("");
		/*for(int i = 0; i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				System.out.print(String.format("%f", mat3[i][j]) + "    ");
			}
			System.out.println("");
		}
		System.out.println("");
		for(int i = 0; i<mat.length;i++) {
			for(int j=0;j<mat[0].length;j++) {
				System.out.print(String.format("%.5f", entropyCal(mat,j,i)) + "\t" + "\t");
			}
			System.out.println("");
		}*/
		int[] values = seamArray(mat,mat3);
		for(int i = 0; i<values.length;i++) {
			System.out.print(values[i]+"  ");
		}
		System.out.println("");
		int[][] mat5=null;
		mat5=ImageManipulation.RemoveSeam2(mat, values);
		double[][] mat4=energyMat(mat5,mat2,values,0);
		for(int i = 0; i<mat5.length;i++) {
			for(int j=0;j<mat5[0].length;j++) {
				System.out.print(String.format("%d", mat5[i][j]) + "\t");
			}
			System.out.println("");
		}
		System.out.println("");
		for(int i = 0; i<mat4.length;i++) {
			for(int j=0;j<mat4[0].length;j++) {
				System.out.print(String.format("%.5f", mat4[i][j]) + "\t");
			}
			System.out.println("");
		}
		System.out.println("");
		int[][] img=image.GetMatrix();
		//img=ImageManipulation.transpose(img);
		double[][] dyn=dynamicPrograming(img,0);
		int[][] kseam =kSeams(img,dyn,100);
		img=removekSeams(img,kseam);
		//img=ImageManipulation.transpose(img);
		ImageOperator.copy(img);
	}
	
	public static int getRed(int pixel) {
		return new Color(pixel).getRed();
	}
	
	public static int getBlue(int pixel) {
		return new Color(pixel).getBlue();
	}
	
	public static int getGreen(int pixel) {
		return new Color(pixel).getGreen();
	}
	
	public static void paintRed(int[][] image, int[] seam) {
		for(int i=0;i<seam.length;i++) {
			image[i][seam[i]]=new Color(255,0,0).getRGB();
		}
	}
	
	/**
	 * 
	 * @param pixel1
	 * @param pixel2
	 * @return the energy value between the two pixels
	 */
	public static double energyCal(int pixel1,int pixel2) {
		int Ri,R1,Gi,G1,Bi,B1;
		Ri=getRed(pixel1);
		Gi=getGreen(pixel1);
		Bi=getBlue(pixel1);
		R1=getRed(pixel2);
		G1=getGreen(pixel2);
		B1=getBlue(pixel2);
		return (Math.abs(Ri-R1)+Math.abs(Bi-B1)+Math.abs(Gi-G1))/3.;
		
	}
	
	public static double Pmn(int[][] imgArray, int m, int n, int x, int y) {
		double sum=0;
		for(int k=x-4;k<=x+4;k++) {
			for(int l=y-4;l<=y+4;l++) {
				if((k>=0 && k<imgArray[0].length) && (l>=0 && l<imgArray.length)) {
					sum+=GreyScale(imgArray,k,l);
				}
			}
		}
		if(sum==0) {
			return 0;
		}
		return GreyScale(imgArray,m,n)/sum;
	}
	
	public static double entropyCal(int[][] imgArray, int x, int y) {
		double sum=0;
		double norm=0;
		double pmn;
		for(int m=x-4;m<=x+4;m++) {
			for(int n=y-4;n<=y+4;n++) {
				if((m>=0 && m<imgArray[0].length) && (n>=0 && n<imgArray.length)) {
					pmn=Pmn(imgArray,m,n,x,y);
					if(pmn==0)
						sum+=0;
					else {
						sum+=pmn*Math.log(pmn);
					}
					norm++;
				}
			}
		
		}
		//System.out.print(" "+norm+" ");
		//return sum/norm;
		return -sum/norm;
	}
	/**
	 * 
	 * @param imgArray
	 * @param energyType - the type of energy calculation with or with not entropy
	 * @param i
	 * @param j
	 * @return the energy value of imgArray[i,j] normalized
	 */
	public static double EnergyFunction(int[][] imgArray, int energyType, int i, int j) {
		double sum=0;
		double norm=0;
		for(int k=i-1;k<=i+1;k++) {
			for(int w=j-1;w<=j+1;w++) {
				if(k!=i || w!=j) {
					if((k>=0 && k<imgArray.length) && (w>=0 && w<imgArray[0].length)) {
						sum+=energyCal(imgArray[i][j],imgArray[k][w]);
						norm++;
					}
				}
			}
		}
		sum = sum/norm;
		if(energyType == 1) {
			sum+= entropyCal(imgArray,j,i);
		}
		return sum;
	}

	
	public static void GreyScale(ImageOperator op) {
		BufferedImage out= new BufferedImage(op.img.getWidth(),op.img.getHeight(),1);
		int [][] mat =op.GetMatrix();
		int blue,green,red,color=0;
		for(int i=0;i<op.img.getHeight();i++) {
			for(int j=0;j<op.img.getWidth();j++) {
				blue=getBlue(mat[i][j]);
				green=getGreen(mat[i][j]);
				red=getRed(mat[i][j]);
				//color=(int) ((0.3 * red) + (0.59 * green) + (0.11 * blue));
				color=(int) ((red + green + blue)/3);
				out.setRGB(j, i, new Color(color, color, color).getRGB());
			}
		}
		File output= new File("greyScale2.jpg");
		try {
			ImageIO.write(out,"jpg",output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @return color - the greyscale value in image[h,w]
	 */
	public static int GreyScale(int[][] image, int w, int h) {
		int blue=getBlue(image[h][w]);
		int green=getGreen(image[h][w]);
		int red=getRed(image[h][w]);
		int color=(int) ((red + green + blue)/3);
		return color;
	}

	
	/**
	 * this function calculate the array with the summing energies using dynamic programming
	 * @param image
	 * @return the energy array
	 */
	public static double[][] dynamicPrograming(int[][] image, int energyType){
		double[][] energyMat = new double[image.length][image[0].length];
		double temp;
		for(int i=0;i<image[0].length;i++) {
			energyMat[0][i]=EnergyFunction(image,energyType,0,i);
		}
		for(int i=1;i<image.length;i++) {
			for(int j=0;j<image[0].length;j++) {
				if(j-1>=0) {
					temp = Math.min(energyMat[i-1][j], energyMat[i-1][j-1]);
					if(j+1<image[0].length) {
						temp = Math.min(temp, energyMat[i-1][j+1]);
					}
				}
				else {
					if(j+1<image[0].length) {
						temp = Math.min(energyMat[i-1][j], energyMat[i-1][j+1]);
					}
					else {
						temp = energyMat[i-1][j];
					}
				}
				energyMat[i][j] = EnergyFunction(image,energyType,i,j)+temp;
			}
		}
		return energyMat;
	}

	public static int[] seamArray(int[][] image, double[][] dynamicEnergy) {
		int[] seam = new int[dynamicEnergy.length];
		double min = dynamicEnergy[dynamicEnergy.length-1][0];
		int index=0;
		for(int i=1; i<dynamicEnergy[0].length;i++) {
			if(dynamicEnergy[dynamicEnergy.length-1][i]<min) {
				min=dynamicEnergy[dynamicEnergy.length-1][i];
				index=i;
			}
		}
		//System.out.println(index);
		seam[seam.length-1]=index;
		//dynamicEnergy[dynamicEnergy.length-1][index]=10000000;
		for(int i=seam.length-2;i>=0;i--) {
			if(seam[i+1]==0) {
				if(dynamicEnergy[i][seam[i+1]]<=dynamicEnergy[i][seam[i+1]+1])
					seam[i]=seam[i+1];
				else
					seam[i]=seam[i+1]+1;
			}
			else if(seam[i+1]==dynamicEnergy[0].length-1) {
				if(dynamicEnergy[i][seam[i+1]]<dynamicEnergy[i][seam[i+1]-1])
					seam[i]=seam[i+1];
				else
					seam[i]=seam[i+1]-1;
			}
			else {
				if(dynamicEnergy[i][seam[i+1]]<dynamicEnergy[i][seam[i+1]-1]) {
					if(dynamicEnergy[i][seam[i+1]]<=dynamicEnergy[i][seam[i+1]+1])
						seam[i]=seam[i+1];
					else
						seam[i]=seam[i+1]+1;
				}
				else {
					if(dynamicEnergy[i][seam[i+1]-1]<=dynamicEnergy[i][seam[i+1]+1]) {
						seam[i]=seam[i+1]-1;
					}
					else
						seam[i]=seam[i+1]+1;
				}
					
			}
			//dynamicEnergy[i][seam[i]]=10000000;
		}
		return seam;
	}

	public static int[][] kSeams(int[][] image,double[][] dynamicEnergy, int k){
		int[][] kseams=new int[k][dynamicEnergy.length];
		for(int i =0;i<k;i++) {
			kseams[i]=seamArray(image,dynamicEnergy);
		}
		return kseams;
	}
	public static int[][] removekSeams(int[][] image, int[][] seamsMat) {
		int[][] image2=new int[image.length][image[0].length-seamsMat.length];
		for(int i =0;i<seamsMat.length;i++) {
			for(int j=0;j<seamsMat[0].length;j++) {
				image[j][seamsMat[i][j]]=-555;
			}
		}
		int sub=0;
		for(int i=0;i<image.length;i++) {
			for(int j=0;j<image[0].length;j++) {
				if(image[i][j]==-555) {
					sub++;
				}
				else {
					//image2[i][j-sub]=image[i][j];
				}
			}
			sub=0;
		}
		return image2;
	}

	public static double[][] energyMat(int[][] image,double[][] imageEnergy,int[] seam,int energyType){
		double[][] energy= new double[imageEnergy.length][imageEnergy[0].length-1];
		if(energyType==0) {
			for(int i =0;i<energy.length;i++) {
				for(int j=0;j<imageEnergy[0].length;j++) {
					if(j<seam[i])
						if(j==seam[i]-1) {
							energy[i][j]=EnergyFunction(image,energyType,i,j);
						}
						else
							energy[i][j]=imageEnergy[i][j];
					else {
						if(j>seam[i]) {
							if(j-1==seam[i])
								energy[i][j-1]=EnergyFunction(image,energyType,i,j-1);
							else
								energy[i][j-1]=imageEnergy[i][j];
						}
					}
				}
			}
		}
		return energy;
	}

}



