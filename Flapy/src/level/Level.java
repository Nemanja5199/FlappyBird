package level;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

import graphics.Shader;
import graphics.Texture;
import graphics.VertexArray;
import input.Input;
import math.Matrix4f;
import math.Vector3f;

public class Level {

	
private VertexArray background , fade;
private Texture bgTexture;
private int xScroll= 0;
private int map=0;

private Bird bird;
private Pipe[] pipes = new Pipe[5*2];

private Random random = new Random();

private float time =0.0f;

private float OFFSET = 5.0f;

private boolean control  = true , reset = false;

private int index = 0;


public Level() {
	float[] vertices = new float[] {
			-10.0f, -10.0f * 9.0f / 16.0f, 0.0f,
			-10.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f,  10.0f * 9.0f / 16.0f, 0.0f,
			  0.0f, -10.0f * 9.0f / 16.0f, 0.0f
			
	};
	
	byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
	float[] tcs = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
	fade= new VertexArray(6);
	background = new VertexArray(vertices, indices, tcs);
	bgTexture = new Texture("res/bg.jpeg");	
	bird = new Bird();
	
	createPipes();

}

private void createPipes() {
	Pipe.create();
	for(int i= 0 ; i<5*2;i+=2) {
		pipes[i] = new Pipe(OFFSET+index*3.0f,random.nextFloat()* 4.0f);
		pipes[i+1] = new Pipe(pipes[i].getX(),pipes[i].getY()-11.5f);
		index +=2;
	}
}
private void UpdatePipes() {
	pipes[index % 10] = new Pipe(OFFSET+index*3.0f,random.nextFloat()* 4.0f);
	pipes[(index+1) % 10] = new Pipe(pipes[index % 10].getX(),pipes[index %10].getY()-11.0f);
	index +=2;
}

public void update() {
	if(control) {
	xScroll--;
	if(-xScroll % 335 == 0) map++;
	if(-xScroll > 250 && -xScroll % 120 == 0)
		UpdatePipes();
	}
	bird.update();
	if(control && collision()) {
		bird.fall();
		control=false;
	}
	
		if(!control && Input.isKeyDown(GLFW_KEY_SPACE) ) {
			
			reset = true;
		}
}

private void renderPipes() {
	Shader.PIPE.enable();
	Shader.PIPE.setUnifromMat4f("vw_matrix", Matrix4f.translate(new Vector3f( xScroll*0.05f,0.0f,0.0f)));
	Pipe.getTexture().bind();
	Pipe.getMesh().bind();
	for(int i=0; i<5*2;i++) {
		Shader.PIPE.setUnifromMat4f("ml_matrix", pipes[i].getModelMatrix());
		Shader.PIPE.setUniform1i("top", i % 2 == 0 ? 1 : 0);//da okrenemo cevi gornje
		Pipe.getMesh().draw();
	}
	Pipe.getMesh().unbind();
	Pipe.getTexture().unbind();
}
private boolean collision() {

	for (int i = 0; i < 5 * 2; i++) {
		float bx = -xScroll * 0.05f;
		float by = bird.getY();
		float px = pipes[i].getX();
		float py = pipes[i].getY();
		
		float bx0 = bx - bird.getSize() / 2.0f;
		float bx1 = bx + bird.getSize() / 2.0f;
		float by0 = by - bird.getSize() / 2.0f;
		float by1 = by + bird.getSize() / 2.0f;
		
		
		float px0 = px;
		float px1 = px + Pipe.getWidth();
		float py0 = py;
		float py1 = py + Pipe.getHeight();
		
		System.out.println(by);
		
		if (bx1 > px0 && bx0 < px1) {
			if (by1 > py0 && by0 < py1) {
				return true;
			}
		}
		if(by < -5 || by> 6) {
			return true ;
		}
		
	}
	return false;
}

public boolean isgameOver() {
	return reset;
}

public void render() {
	bgTexture.bind();
	Shader.BG.enable();
	background.bind();
	for(int i = map; i<map+4 ;i++) {
		Shader.BG.setUnifromMat4f("vw_matrix", Matrix4f.translate(new Vector3f(i*10 + xScroll*0.03f,0.0f,0.0f)));
		background.draw();
	}
	
	Shader.BG.disable();
	bgTexture.unbind();
	
	
	renderPipes();
	bird.render();
	
	
}
		
}
