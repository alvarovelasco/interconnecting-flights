package com.ryanair.alvaro.interconnectingflights;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.ryanair.alvaro.interconnectingflights.logic.ScheduleAbsoluteRouteInterconnectionResolverImplService;

@RunWith(MockitoJUnitRunner.class)
public class ScheduleAbsoluteRouteInterconnectionResolverUtilTest {

	@Test
	public void testSameYearMonthPeriod() {
		List<YearMonth> yearMonths = ScheduleAbsoluteRouteInterconnectionResolverImplService.getAllYearMonthIn(LocalDate.of(2018, 6, 1),
				LocalDate.of(2018, 6, 29));
		assertEquals(1, yearMonths.size());
		assertEquals(YearMonth.of(2018, 6), yearMonths.get(0));
	}

	@Test
	public void testYearMonthThreeMonthPeriod() {
		List<YearMonth> yearMonths = ScheduleAbsoluteRouteInterconnectionResolverImplService.getAllYearMonthIn(LocalDate.of(2018, 6, 1),
				LocalDate.of(2018, 9, 29));
		assertEquals(4, yearMonths.size());
		assertEquals(YearMonth.of(2018, 9), yearMonths.get(3));
	}

	@Test
	public void testYearMonthTwoMonthInDifferentYearPeriod() {
		List<YearMonth> yearMonths = ScheduleAbsoluteRouteInterconnectionResolverImplService.getAllYearMonthIn(LocalDate.of(2018, 12, 1),
				LocalDate.of(2019, 1, 29));
		assertEquals(2, yearMonths.size());
		assertEquals(YearMonth.of(2019, 1), yearMonths.get(1));
	}

	@Test
	public void testYearMonthPeriodWithinReverseRange() {
		List<YearMonth> yearMonths = ScheduleAbsoluteRouteInterconnectionResolverImplService.getAllYearMonthIn(LocalDate.of(2018, 12, 1),
				LocalDate.of(2019, 11, 29));
		assertEquals(12, yearMonths.size());
		assertEquals(YearMonth.of(2019, 11), yearMonths.get(11));
	}

}
