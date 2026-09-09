/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import static org.wpilib.units.Units.*;

import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorPorts;
import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorSettings;
import com.stuypulse.robot.util.talonfx.sim.SystemSim;
import com.stuypulse.robot.util.talonfx.sim.TalonFXSimulation;

import org.wpilib.math.system.DCMotor;
import org.wpilib.math.system.Models;
import org.wpilib.simulation.ElevatorSim;

import com.ctre.phoenix6.StatusCode;

public final class ElevatorIOSim extends ElevatorIOBase {
    private final SystemSim<ElevatorSim> elevatorSim;

    private final TalonFXSimulation TRMotor; // top right motor (leader)
    private final TalonFXSimulation BRMotor; // botoom right
    private final TalonFXSimulation BLMotor; // bottom left
    private final TalonFXSimulation TLMotor; // top left

    public ElevatorIOSim() {
        final SystemSim<ElevatorSim> elevatorSim = SystemSim.of(new ElevatorSim(
                Models.elevatorFromPhysicalConstants(
                        DCMotor.getKrakenX60Foc(4), ElevatorSettings.MOVING_MASS.in(Kilograms),
                        ElevatorSettings.DRUM_RADIUS.in(Meters), ElevatorSettings.GEAR_RATIO),
                DCMotor.getKrakenX60Foc(4), ElevatorSettings.MIN_HEIGHT.in(Meters),
                ElevatorSettings.MAX_HEIGHT.in(Meters), true, ElevatorSettings.MIN_HEIGHT.in(Meters)), ElevatorSettings.DRUM_RADIUS);
        final TalonFXSimulation TRMotor = new TalonFXSimulation(ElevatorPorts.TR_MOTOR, ElevatorSettings.GEAR_RATIO, elevatorSim);
        final TalonFXSimulation BRMotor = new TalonFXSimulation(ElevatorPorts.BR_MOTOR, ElevatorSettings.GEAR_RATIO, elevatorSim);
        final TalonFXSimulation BLMotor = new TalonFXSimulation(ElevatorPorts.BL_MOTOR, ElevatorSettings.GEAR_RATIO, elevatorSim);
        final TalonFXSimulation TLMotor = new TalonFXSimulation(ElevatorPorts.TL_MOTOR, ElevatorSettings.GEAR_RATIO, elevatorSim);

        super(TRMotor, BRMotor, BLMotor, TLMotor);

        this.elevatorSim = elevatorSim;

        this.TRMotor = TRMotor;
        this.BRMotor = BRMotor;
        this.BLMotor = BLMotor;
        this.TLMotor = TLMotor;
    }

    @Override
    public StatusCode updateInputs(final ElevatorInputs inputs) {
        elevatorSim.update(GlobalSettings.DT);

        TRMotor.refresh();
        BRMotor.refresh();
        BLMotor.refresh();
        TLMotor.refresh();

        return super.updateInputs(inputs);
    }
}
