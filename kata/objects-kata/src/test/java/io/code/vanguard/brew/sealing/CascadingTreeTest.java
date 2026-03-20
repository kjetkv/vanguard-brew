package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Objects;

import static io.code.vanguard.brew.Extractors.parentInterfaceForClass;
import static io.code.vanguard.brew.sealing.CascadingTreeKata.Circle;
import static io.code.vanguard.brew.sealing.CascadingTreeKata.Polygon;
import static io.code.vanguard.brew.sealing.CascadingTreeKata.Shape;
import static io.code.vanguard.brew.sealing.CascadingTreeKata.Square;
import static io.code.vanguard.brew.sealing.CascadingTreeKata.Triangle;

@DisplayName("Sealing - Cascading Tree")
@Tag("Sealing")
public class CascadingTreeTest extends BasicKataTestBase {

    @Test
    @DisplayName("Shape must be a sealed interface.")
    @Order(1)
    void testShapeIsSealed() {
        verifyClass(
                Shape.class,
                Class::isSealed,
                true,
                Objects::equals
        );
    }


    @Test
    @DisplayName("Circle must be explicitly final.")
    @Order(2)
    void testCircleIsFinal() {
        verifyClass(
                Circle.class,
                clazz -> Modifier.isFinal(clazz.getModifiers()),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Circle must implement Shape.")
    @Order(3)
    void testCircleInheritance() {
        verifyClass(
                Circle.class,
                parentInterfaceForClass,
                "Parent-interface: Shape",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Polygon must be an abstract class.")
    @Order(4)
    void testPolygonIsAbstract() {
        verifyClass(
                Polygon.class,
                clazz -> Modifier.isAbstract(clazz.getModifiers()),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Polygon must be sealed to continue the cascade.")
    @Order(5)
    void testPolygonIsSealed() {
        verifyClass(
                Polygon.class,
                Class::isSealed,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Polygon must implement Shape.")
    @Order(6)
    void testPolygonInheritance() {
        verifyClass(
                Polygon.class,
                parentInterfaceForClass,
                "Parent-interface: Shape",
                Objects::equals
        );
    }


    @Test
    @DisplayName("Square must be explicitly final.")
    @Order(7)
    void testSquareIsFinal() {
        verifyClass(
                Square.class,
                clazz -> Modifier.isFinal(clazz.getModifiers()),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Square must inherit from Polygon.")
    @Order(8)
    void testSquareInheritance() {
        verifyClass(
                Square.class,
                clazz -> "Parent-class: " + clazz.getSuperclass().getSimpleName(),
                "Parent-class: Polygon",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Triangle must be explicitly non-sealed.")
    @Order(9)
    void testTriangleIsNonSealed() {
        verifyClass(
                Triangle.class,
                clazz -> !clazz.isSealed() && !Modifier.isFinal(clazz.getModifiers()),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Triangle must inherit from Polygon.")
    @Order(10)
    void testTriangleInheritance() {
        verifyClass(
                Triangle.class,
                clazz -> "Parent-class: " + clazz.getSuperclass().getSimpleName(),
                "Parent-class: Polygon",
                Objects::equals
        );
    }
}