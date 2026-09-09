/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import static org.wpilib.units.Units.KilogramSquareMeters;
import static org.wpilib.units.Units.Meters;
import static org.wpilib.units.Units.Radians;

import org.wpilib.math.system.DCMotor;
import org.wpilib.math.system.Models;
import org.wpilib.simulation.DCMotorSim;
import org.wpilib.simulation.SingleJointedArmSim;

import com.ctre.phoenix6.StatusCode;
import com.stuypulse.robot.constants.GlobalSettings;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawPorts;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawSettings;
import com.stuypulse.robot.util.talonfx.sim.SystemSim;
import com.stuypulse.robot.util.talonfx.sim.TalonFXSimulation;

public final class ClawIOSim extends ClawIOBase {
    private final SystemSim<SingleJointedArmSim> pivotSim;
    private final SystemSim<DCMotorSim> rollerSim;

    private final TalonFXSimulation clawPivotMotor;
    private final TalonFXSimulation clawRollerMotor;

    public ClawIOSim() {
        final SystemSim<SingleJointedArmSim> pivotSim = SystemSim.of(
            new SingleJointedArmSim(
                DCMotor.getKrakenX60Foc(1), 
                ClawSettings.Pivot.GEAR_RATIO, 
                ClawSettings.Pivot.MOI.in(KilogramSquareMeters), 
                ClawSettings.Pivot.ARM_LENGTH.in(Meters), 
                ClawSettings.Pivot.MIN_ANGLE.in(Radians), 
                ClawSettings.Pivot.MAX_ANGLE.in(Radians), 
                true, 
                ClawSettings.Pivot.MIN_ANGLE.in(Radians)
            )
        );
        final TalonFXSimulation clawPivotMotor = new TalonFXSimulation(ClawPorts.CLAW_PIVOT_MOTOR, ClawSettings.Pivot.GEAR_RATIO, pivotSim);

        final SystemSim<DCMotorSim> rollerSim = SystemSim.of(
            new DCMotorSim(
                Models.singleJointedArmFromPhysicalConstants(
                    DCMotor.getKrakenX60Foc(1), 
                    ClawSettings.Rollers.MOI.in(KilogramSquareMeters), 
                    ClawSettings.Rollers.GEAR_RATIO
                ), 
                DCMotor.getKrakenX44Foc(1)
            )
        );
        final TalonFXSimulation clawRollerMotor = new TalonFXSimulation(ClawPorts.CLAW_ROLLER_MOTOR, ClawSettings.Rollers.GEAR_RATIO, rollerSim);
        
        super(clawPivotMotor, clawRollerMotor);
        
        this.pivotSim = pivotSim;
        this.clawPivotMotor = clawPivotMotor;
        
        this.rollerSim = rollerSim;
        this.clawRollerMotor = clawRollerMotor;
    }

    @Override
    public StatusCode updateInputs(final ClawInputs inputs) {
        pivotSim.update(GlobalSettings.DT);
        clawPivotMotor.refresh();

        rollerSim.update(GlobalSettings.DT);
        clawRollerMotor.refresh();

        return super.updateInputs(inputs);
    }
}
