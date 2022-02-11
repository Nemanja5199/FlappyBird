
package math;

import java.nio.FloatBuffer;

import utils.BufferUtils;



public class Matrix4f {

	public static final int SIZE = 4*4;
	public float[] elements = new float[SIZE];
	
	
	public Matrix4f() {
		
	}
	
	public static Matrix4f identity() {
		
		Matrix4f rezult = new Matrix4f();
		for(int i=0;i<SIZE;i++) {
			rezult.elements[i]=0.0f;
		}
		rezult.elements[0+0 *4]=1.0f;// Dijagonalno postavljamo matricu na vrednost 1 odnosno Jedinicna matrica
		rezult.elements[1+1 *4]=1.0f;
		rezult.elements[2+2 *4]=1.0f;
		rezult.elements[3+3 *4]=1.0f;
		
		return rezult;
		
	}
	
	public static Matrix4f orthographic(float left,float right,float bottom,float top,float near,float far) {
		//Ovo je matrica pomocu koje kreiramo proekciju nasih objekata na prozoru igrice
		Matrix4f rezult = identity();
		rezult.elements[0 + 0 *4]= 2.0f/(right-left);
		rezult.elements[1 + 1 *4]= 2.0f/(top-bottom);
		rezult.elements[2 + 2 *4]= 2.0f/(near-far);
		
		rezult.elements[0 + 3 *4]= (left+right)/(left-right);
		rezult.elements[1 + 3 *4]= (bottom+top)/(bottom-top);
		rezult.elements[2 + 3 *4]= (far+near)/(far-near);
		return rezult;
		
		
		
	}
	
	public static Matrix4f translate(Vector3f vector) {
		// Ovde kreiramo nasu matircu pomocu naseg vektora
		Matrix4f result= identity();
		result.elements[0+3*4]=vector.x;
		result.elements[1+3*4]=vector.y;
		result.elements[2+3*4]=vector.z;
		return result;
		
	}
	
	public static Matrix4f rotate(float angle) {
		//Ovde vrismo rotaciju pomocu sinusa i kosunusa to moze se videti kod ptice kada pada 
		Matrix4f result = identity();
		float r = (float)Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		
		result.elements[0 + 0 *4]=cos;
		result.elements[1 + 0 *4]=sin;
		
		result.elements[0 + 1 *4]=-sin;
		result.elements[1 + 1 *4]=cos;
		return result;
	}
	
	public Matrix4f multiply(Matrix4f matrix)
	{			
		// klasinko matricno mnozenje 
		Matrix4f result =new Matrix4f();
		for(int y = 0; y<4 ; y++) {
			for(int x = 0; x<4 ; x++) {
				float sum = 0.0f;
				for(int e = 0; e<4 ; e++) {
					sum+=this.elements[x +e *4]	* matrix.elements[e + y *4];
					
				}
				result.elements[x + y *4]=sum;
				
			}
		}
		return result;
		
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}
}
