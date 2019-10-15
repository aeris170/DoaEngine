package com.doa.maths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

class DoaVectorDTest {

	@Test
	void addTest() {
		fail("Not yet implemented");
	}

	@Test
	void subTest() {
		fail("Not yet implemented");
	}

	@Test
	void mulTest_GIVEN_ArgumenIsDouble() {
		fail("Not yet implemented");
	}

	@Test
	void translateTest() {
		fail("Not yet implemented");
	}

	@Test
	void rotateTest() {
		fail("Not yet implemented");
	}

	@Test
	void mulTest_GIVEN_ArgumentIsDoaVectorD() {
		fail("Not yet implemented");
	}

	@Test
	void negateTest() {

	}

	@Test
	void normaliseTest() {

	}

	@Test
	void normTest() {
		DoaVectorD v = new DoaVectorD(0, 10);
		double expected = Math.sqrt(10);
		double actual = v.norm();
		assertEquals(expected, actual);
		v.x = 7;
		v.y = 24;
		expected = 5;
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 5;
		v.y = 12;
		expected = Math.sqrt(13);
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 3;
		v.y = 4;
		expected = Math.sqrt(5);
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 8;
		v.y = 15;
		expected = Math.sqrt(17);
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 9;
		v.y = 40;
		expected = Math.sqrt(41);
		actual = v.norm();
		assertEquals(expected, actual);
	}

	@Test
	void normSquaredTest() {
		DoaVectorD v = new DoaVectorD(0, 10);
		int expected = 10;
		double actual = v.norm();
		assertEquals(expected, actual);
		v.x = 7;
		v.y = 24;
		expected = 25;
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 5;
		v.y = 12;
		expected = 13;
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 3;
		v.y = 4;
		expected = 5;
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 8;
		v.y = 15;
		expected = 17;
		actual = v.norm();
		assertEquals(expected, actual);
		v.x = 9;
		v.y = 40;
		expected = 41;
		actual = v.norm();
		assertEquals(expected, actual);
	}

	@Test
	void cloneTest() {
		DoaVectorD v = new DoaVectorD(321, 91);
		DoaVectorD clone = v.clone();
		assertNotSame(v, clone);
		assertEquals(v, clone);
		v.x++;
		assertNotEquals(v, clone);
	}

	@Test
	void equalsAndHashCodeTest() {
		DoaVectorD v1 = new DoaVectorD(321, 91);
		DoaVectorD v2 = new DoaVectorD(321, 91);
		assertNotSame(v1, v2);
		assertTrue(v1.equals(v2) && v2.equals(v1));
		assertEquals(v1.hashCode(), v2.hashCode());
	}
}
