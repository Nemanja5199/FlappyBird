package main;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import graphics.Shader;
import input.Input;
import level.Level;
import math.Matrix4f;


public class Main implements Runnable {

	private int width =1280;
	private int height = 720;
	
	private Thread thread;
	private boolean running=false;
	
	private long window;
	
	private Level level;
	
		
	public void start() {
		 
		running = true;
		thread= new  Thread(this,"Game");
		thread.start();
	
	}
	private void init() {
		if(glfwInit() != true) {
			return;
		
			
		}
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(width, height, "Flapy", NULL, NULL);
		
		if(window == NULL) {
			//TODO-handle
			return;
		}
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width()-width)/2, (vidmode.height()-height)/2);
		
		glfwSetKeyCallback(window, new Input());
		glfwMakeContextCurrent(window);
		glfwShowWindow(window);
		
		GL.createCapabilities();
		
		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		
		glActiveTexture(GL_TEXTURE1);
		
		
		System.out.println("OpenGl:" + glGetString(GL_VERSION));
		Shader.loadAll();
		
		Matrix4f pr_matirx=Matrix4f.orthographic(-10.0f, 10.0f, -10.0f*9.0f/16.0f, 10.0f*9.0f/16.0f,-1.0f,1.0f);
		
		Shader.BG.setUnifromMat4f("pr_matrix", pr_matirx);
		Shader.BG.setUniform1i("tex", 1);
		
		Shader.BIRD.setUnifromMat4f("pr_matrix", pr_matirx);
		Shader.BIRD.setUniform1i("tex", 1);
		
		Shader.PIPE.setUnifromMat4f("pr_matrix", pr_matirx);
		Shader.PIPE.setUniform1i("tex", 1);
		
		
		
		
		
		
		Shader.BG.setUniform1i("tex", 1);
		
		
		
		
		level = new Level();
		
	}
	
	
	

	
	
	public void run() {
		init();
		long lastTime = System.nanoTime();
		double ns = 1000000000.0/60.0;
		long timer = System.currentTimeMillis();
		double delta = 0.0;
		int updates = 0;
		int frames = 0;
		while(running) {
			long now = System.nanoTime();
			delta +=(now -lastTime)/ns;
			lastTime = now ;
			if(delta >=1.0) {
				update();
				updates++;
				delta--;
			}
		
			render();
			frames++;
			if(System.currentTimeMillis() - timer > 1000) {
				timer +=1000;
				System.out.println(updates+ "ups,"+ frames + "fps");
				updates = 0;
				frames=0;
			}
			
			if(glfwWindowShouldClose(window) == true )
				running=false;
		}
		glfwDestroyWindow(window);
		glfwTerminate();
		
	}
	
	private void update() {
	
		glfwPollEvents();
		level.update();
		if(level.isgameOver()) {
			level= new Level();
		}
		
		
	}
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		level.render();
		int error=glGetError();
		if(error!= GL_NO_ERROR) {
			System.out.println(error);
		}
		glfwSwapBuffers(window);
		
	}
	
	public static void main(String[] args) {
		new Main().start();
		

	}

}
