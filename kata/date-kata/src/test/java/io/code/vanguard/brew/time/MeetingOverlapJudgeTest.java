package io.code.vanguard.brew.time;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Objects;

import static io.code.vanguard.brew.time.MeetingOverlapJudgeKata.MeetingRequest;

@DisplayName("Time - Meeting Overlap Judge")
@Tag("Time")
public class MeetingOverlapJudgeTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the second meeting starts while the first is still running, returns true. " +
            "E.g.: A(09:00-10:30), B(10:00-11:30) returns true")
    @Order(1)
    void testStandardOverlap() {
        verifyBasicKata(new MeetingOverlapJudgeKata(),
                new MeetingRequest(
                        LocalTime.of(8, 15), LocalTime.of(10, 15),
                        LocalTime.of(9, 30), LocalTime.of(11, 30)
                ),
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When the first meeting ends completely before the second one begins, returns false. " +
            "E.g.: A(13:00-14:00), B(15:00-16:00) returns false")
    @Order(2)
    void testNoOverlap() {
        verifyBasicKata(new MeetingOverlapJudgeKata(),
                new MeetingRequest(
                        LocalTime.of(13, 37), LocalTime.of(14, 0),
                        LocalTime.of(15, 0), LocalTime.of(16, 0)
                ),
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When one meeting ends at the exact same minute the next one starts, returns false. " +
            "E.g.: A(11:00-12:00), B(12:00-13:00) returns false")
    @Order(3)
    void testExactBoundary() {
        verifyBasicKata(new MeetingOverlapJudgeKata(),
                new MeetingRequest(
                        LocalTime.of(11, 0), LocalTime.of(12, 0),
                        LocalTime.of(12, 0), LocalTime.of(13, 0)
                ),
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When one meeting is scheduled entirely inside the other meeting, returns true. " +
            "E.g.: A(08:00-17:00), B(12:00-13:00) returns true")
    @Order(4)
    void testContainedMeeting() {
        verifyBasicKata(new MeetingOverlapJudgeKata(),
                new MeetingRequest(
                        LocalTime.of(9, 0), LocalTime.of(17, 0),
                        LocalTime.of(12, 30), LocalTime.of(13, 30)
                ),
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When the second meeting is evaluated first and ends before the first begins, returns false. " +
            "E.g.: A(18:00-19:00), B(16:00-17:00) returns false")
    @Order(5)
    void testReversedOrderNoOverlap() {
        verifyBasicKata(new MeetingOverlapJudgeKata(),
                new MeetingRequest(
                        LocalTime.of(20, 0), LocalTime.of(22, 0),
                        LocalTime.of(18, 0), LocalTime.of(19, 30)
                ),
                false,
                Objects::equals);
    }
}