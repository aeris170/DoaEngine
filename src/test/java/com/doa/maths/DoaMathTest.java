package com.doa.maths;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DoaMathTest {

	@Test
	void clampTest_GIVEN_ValueLargerThanUpperBound_WHEN_ValueIsInt() {
		int expected = 5;
		int actual = DoaMath.clamp(684, -5, 5);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueBetweenTheBounds_WHEN_ValueIsInt() {
		int expected = 3;
		int actual = DoaMath.clamp(3, -5, 5);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueSmallerThanTheLowerBound_WHEN_ValueIsInt() {
		int expected = -5;
		int actual = DoaMath.clamp(-10, -5, 5);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueLargerThanUpperBound_WHEN_ValueIsFloat() {
		float expected = 3.16f;
		float actual = DoaMath.clamp(46f, -.3f, 3.16f);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueBetweenTheBounds_WHEN_ValueIsFloat() {
		float expected = 1f;
		float actual = DoaMath.clamp(1f, -.3f, 3.16f);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueSmallerThanTheLowerBound_WHEN_ValueIsFloat() {
		float expected = -3f;
		float actual = DoaMath.clamp(-155f, -3f, 5f);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueLargerThanUpperBound_WHEN_ValueIsDouble() {
		double expected = 3.16;
		double actual = DoaMath.clamp(46, -.3f, 3.16);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueBetweenTheBounds_WHEN_ValueIsDouble() {
		double expected = 1;
		double actual = DoaMath.clamp(1, -.3, 3.16);
		assertEquals(expected, actual);
	}

	@Test
	void clampTest_GIVEN_ValueSmallerThanTheLowerBound_WHEN_ValueIsDouble() {
		double expected = -3;
		double actual = DoaMath.clamp(-155, -3, 5);
		assertEquals(expected, actual);
	}

	@Test
	void lerpTest_WHEN_ParametersAreFloat() {
		float expected = -5f;
		float actual = DoaMath.lerp(-20, 10, .5);
		assertEquals(expected, actual);
	}

	@Test
	void lerpTest_WHEN_ParametersAreDouble() {
		double expected = 0;
		double actual = DoaMath.lerp(-10, 10, .5);
		assertEquals(expected, actual);
	}

	@Test
	void mapTest_WHEN_ValueIsInt() {
		int expected = 100;
		float actual = DoaMath.map(5, 0, 5, 0, 100);
		assertEquals(expected, actual);
		expected = 50;
		actual = DoaMath.map(0, -10, 10, 0, 100);
		assertEquals(expected, actual);
		expected = -50;
		actual = DoaMath.map(0, -10, 10, -100, 0);
		assertEquals(expected, actual);
	}

	@Test
	void mapTest_WHEN_ValueIsFloat() {
		int expected = 1440;
		float actual = DoaMath.map(.5f, -1, 1, 0, 1920);
		assertEquals(expected, actual);
		expected = 480;
		actual = DoaMath.map(-.5f, -1, 1, 0, 1920);
		assertEquals(expected, actual);
		expected = 960;
		actual = DoaMath.map(0f, -1, 1, 0, 1920);
		assertEquals(expected, actual);
	}

	@Test
	void mapTest_WHEN_ValueIsDouble() {
		int expected = 1440;
		double actual = DoaMath.map(.5, -1, 1, 0, 1920);
		assertEquals(expected, actual);
		expected = 480;
		actual = DoaMath.map(-.5, -1, 1, 0, 1920);
		assertEquals(expected, actual);
		expected = 960;
		actual = DoaMath.map(0d, -1, 1, 0, 1920);
		assertEquals(expected, actual);
	}
}
