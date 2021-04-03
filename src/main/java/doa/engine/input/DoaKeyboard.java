package doa.engine.input;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import doa.engine.Internal;

/**
 * Responsible for catching all fired keyboard inputs at any logical frame. This
 * class is static, therefore has no objects.
 *
 * @author Doga Oruc
 * @since DoaEngine 1.0
 * @version 3.0
 */
public final class DoaKeyboard {

	/**
	 * Constructor.
	 */
	private DoaKeyboard() {}

	/**
	 * This construct ensures all key events in any logical frame to be captured and
	 * saved in this class' internal state. At any logical frame, a reference to any
	 * of this class' fields will return true if and only if that reference mask has
	 * been produced according to a captured key event. Informally, if a key has
	 * been pressed at any time, at that time the respective field will be set to
	 * true and will stay true as long as the key is held down, only when the same
	 * key has been released it is then corresponding field will be set to false.
	 * 
	 * @hidden
	 * @see java.awt.event.KeyAdapter
	 */
	@Internal
	public static final KeyAdapter INPUT = new KeyAdapter() {

		@Override
		public synchronized void keyPressed(final KeyEvent e) { press[e.getKeyCode()] = true; }

		@Override
		public synchronized void keyReleased(final KeyEvent e) { press[e.getKeyCode()] = false; }
	};

	/* VK_WINDOWS IS 524, the array is upper bounded by VK_WINDOWS */
	static boolean[] press = new boolean[525];

	/**
	 * @hidden
	 */
	@Internal
	public static synchronized void tick() {
		ESCAPE = press[KeyEvent.VK_ESCAPE];
		F1 = press[KeyEvent.VK_F1];
		F2 = press[KeyEvent.VK_F2];
		F3 = press[KeyEvent.VK_F3];
		F4 = press[KeyEvent.VK_F4];
		F5 = press[KeyEvent.VK_F5];
		F6 = press[KeyEvent.VK_F6];
		F7 = press[KeyEvent.VK_F7];
		F8 = press[KeyEvent.VK_F8];
		F9 = press[KeyEvent.VK_F9];
		F10 = press[KeyEvent.VK_F10];
		F11 = press[KeyEvent.VK_F11];
		F12 = press[KeyEvent.VK_F12];
		PRINT_SCREEN = press[KeyEvent.VK_PRINTSCREEN];
		SCROLL_LOCK = press[KeyEvent.VK_SCROLL_LOCK];
		PAUSE_BREAK = press[KeyEvent.VK_PAUSE];
		TILDE = press[KeyEvent.VK_DEAD_TILDE];
		ZERO = press[KeyEvent.VK_0];
		ONE = press[KeyEvent.VK_1];
		TWO = press[KeyEvent.VK_2];
		THREE = press[KeyEvent.VK_3];
		FOUR = press[KeyEvent.VK_4];
		FIVE = press[KeyEvent.VK_5];
		SIX = press[KeyEvent.VK_6];
		SEVEN = press[KeyEvent.VK_7];
		EIGHT = press[KeyEvent.VK_8];
		NINE = press[KeyEvent.VK_9];
		BACK_SPACE = press[KeyEvent.VK_BACK_SPACE];
		INSERT = press[KeyEvent.VK_INSERT];
		DELETE = press[KeyEvent.VK_DELETE];
		HOME = press[KeyEvent.VK_HOME];
		END = press[KeyEvent.VK_END];
		PAGE_UP = press[KeyEvent.VK_PAGE_UP];
		PAGE_DOWN = press[KeyEvent.VK_PAGE_DOWN];
		A = press[KeyEvent.VK_A];
		B = press[KeyEvent.VK_B];
		C = press[KeyEvent.VK_C];
		D = press[KeyEvent.VK_D];
		E = press[KeyEvent.VK_E];
		F = press[KeyEvent.VK_F];
		G = press[KeyEvent.VK_G];
		H = press[KeyEvent.VK_H];
		I = press[KeyEvent.VK_I];
		J = press[KeyEvent.VK_J];
		K = press[KeyEvent.VK_K];
		L = press[KeyEvent.VK_L];
		M = press[KeyEvent.VK_M];
		N = press[KeyEvent.VK_N];
		O = press[KeyEvent.VK_O];
		P = press[KeyEvent.VK_P];
		Q = press[KeyEvent.VK_Q];
		R = press[KeyEvent.VK_R];
		S = press[KeyEvent.VK_S];
		T = press[KeyEvent.VK_T];
		U = press[KeyEvent.VK_U];
		V = press[KeyEvent.VK_V];
		W = press[KeyEvent.VK_W];
		X = press[KeyEvent.VK_X];
		Y = press[KeyEvent.VK_Y];
		Z = press[KeyEvent.VK_Z];
		TAB = press[KeyEvent.VK_TAB];
		CAPS_LOCK = press[KeyEvent.VK_CAPS_LOCK];
		SHIFT = press[KeyEvent.VK_SHIFT];
		CTRL = press[KeyEvent.VK_CONTROL];
		ALT = press[KeyEvent.VK_ALT];
		NUM_1 = press[KeyEvent.VK_NUMPAD1];
		NUM_2 = press[KeyEvent.VK_NUMPAD2];
		NUM_3 = press[KeyEvent.VK_NUMPAD3];
		NUM_4 = press[KeyEvent.VK_NUMPAD4];
		NUM_5 = press[KeyEvent.VK_NUMPAD5];
		NUM_6 = press[KeyEvent.VK_NUMPAD6];
		NUM_7 = press[KeyEvent.VK_NUMPAD7];
		NUM_8 = press[KeyEvent.VK_NUMPAD8];
		NUM_9 = press[KeyEvent.VK_NUMPAD9];
		NUM_0 = press[KeyEvent.VK_NUMPAD0];
		DEL = press[KeyEvent.VK_DELETE];
		ENTER = press[KeyEvent.VK_ENTER];
		SPACE = press[KeyEvent.VK_SPACE];
		SLASH = press[KeyEvent.VK_SLASH];
		STAR = press[KeyEvent.VK_MULTIPLY];
		DASH = press[KeyEvent.VK_SUBTRACT];
		PLUS = press[KeyEvent.VK_PLUS];
		KEY_UP = press[KeyEvent.VK_UP];
		KEY_DOWN = press[KeyEvent.VK_DOWN];
		KEY_LEFT = press[KeyEvent.VK_LEFT];
		KEY_RIGHT = press[KeyEvent.VK_RIGHT];
		WINDOWS = press[KeyEvent.VK_WINDOWS];
	}

	/**
	 * @see java.awt.event.KeyEvent
	 */
	public static volatile boolean ESCAPE, F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12, PRINT_SCREEN, SCROLL_LOCK, PAUSE_BREAK, TILDE, ZERO, ONE, TWO,
	        THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, BACK_SPACE, INSERT, DELETE, HOME, END, PAGE_UP, PAGE_DOWN, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O,
	        P, Q, R, S, T, U, V, W, X, Y, Z, TAB, CAPS_LOCK, SHIFT, CTRL, WINDOWS, ALT, NUM_1, NUM_2, NUM_3, NUM_4, NUM_5, NUM_6, NUM_7, NUM_8, NUM_9, NUM_0,
	        DEL, ENTER, SPACE, SLASH, STAR, DASH, PLUS, KEY_UP, KEY_DOWN, KEY_RIGHT, KEY_LEFT;
}
