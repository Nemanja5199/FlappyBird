package utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {

	private BufferUtils() {

	}

	public static ByteBuffer createByteBuffer(byte[] array) {

		ByteBuffer result = ByteBuffer.allocateDirect(array.length).order(ByteOrder.nativeOrder());

		result.put(array).flip();
		return result;
	}

	public static FloatBuffer createFloatBuffer(float[] array) {
		// koristimo <<2 posto nam treba veci protor za float i int buffere
		FloatBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder())
				.asFloatBuffer();

		result.put(array).flip();
		return result;
	}
	public static IntBuffer createIntBuffer(int[] array) {

		IntBuffer result = ByteBuffer.allocateDirect(array.length << 2).order(ByteOrder.nativeOrder())
			.asIntBuffer();

		result.put(array).flip();
		return result;
	}
}
