package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.sealing.ArchitecturalInspectorKata.Car;
import io.code.vanguard.brew.sealing.ArchitecturalInspectorKata.Truck;
import io.code.vanguard.brew.sealing.ArchitecturalInspectorKata.Vehicle;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Objects;

@DisplayName("Sealing - Architectural Inspector")
@Tag("Sealing")
public class ArchitecturalInspectorTest extends BasicKataTestBase {

    @Test
    @DisplayName("Vehicle must be a sealed class.")
    @Order(1)
    void testVehicleIsSealed() {
        verifyClass(
                Vehicle.class,
                Class::isSealed,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Car must be explicitly final.")
    @Order(2)
    void testCarIsFinal() {
        verifyClass(
                Car.class,
                clazz -> Modifier.isFinal(clazz.getModifiers()),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Car must still inherit from Vehicle.")
    @Order(3)
    void testCarInheritance() {
        verifyClass(
                Car.class,
                clazz -> "Parent-class: " + clazz.getSuperclass().getSimpleName(),
                "Parent-class: Vehicle",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Truck must be explicitly non-sealed (neither final nor sealed).")
    @Order(4)
    void testTruckIsNonSealed() {
        verifyClass(
                Truck.class,
                clazz -> !clazz.isSealed() && !Modifier.isFinal(clazz.getModifiers()),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Truck must still inherit from Vehicle.")
    @Order(5)
    void testTruckInheritance() {
        verifyClass(
                Truck.class,
                clazz -> "Parent-class: " + clazz.getSuperclass().getSimpleName(),
                "Parent-class: Vehicle",
                Objects::equals
        );
    }
}