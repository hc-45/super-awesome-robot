/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.hardware.TalonFX;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawSettings;

public class ClawIOBase implements ClawIO {
    private final TalonFX gripperMotor;
    private final TalonFX rollerMotor;
    private final TalonFX pivotMotor;

    private final VoltageOut voltageController;
    private final MotionMagicVoltage motionProfileController;
    private final DutyCycleOut rollerDutyCycleController;

    private final StatusSignal<Current> pivotMotorSupplyCurrent;
    private final StatusSignal<Current> pivotMotorStatorCurrent;
    private final StatusSignal<Angle> pivotMotorPosition;
    private final StatusSignal<Voltage> pivotMotorVoltage;
    private final StatusSignal<Boolean> pivotMotorMotionMagicAtTarget;
    
    public ClawIOBase(final TalonFX gripperMotor, final TalonFX rollerMotor, final TalonFX pivotMotor) {
        this.gripperMotor = gripperMotor;
        this.rollerMotor = rollerMotor;
        this.pivotMotor = pivotMotor;

        this.voltageController = new VoltageOut(ClawSettings.Gripper.IDLE_VOLTAGE);
        this.motionProfileController = new MotionMagicVoltage(ClawSettings.Gripper.OPEN_ANGLE);
        this.rollerDutyCycleController = new DutyCycleOut(ClawSettings.Rollers.IDLE_DUTY_CYCLE);

        this.pivotMotorSupplyCurrent = this.pivotMotor.getSupplyCurrent();
        this.pivotMotorStatorCurrent = this.pivotMotor.getStatorCurrent();
        this.pivotMotorPosition = this.pivotMotor.getPosition();
        this.pivotMotorVoltage = this.pivotMotor.getMotorVoltage(true);
        this.pivotMotorMotionMagicAtTarget = this.pivotMotor.getMotionMagicAtTarget();

        // todo: gripper & roller input sognals
    }

    @Override
    public StatusCode updateInputs(final ClawInputs inputs) {
        final StatusCode refreshStatusCode = BaseStatusSignal.refreshAll(pivotMotorSupplyCurrent, pivotMotorStatorCurrent, pivotMotorPosition, pivotMotorVoltage, pivotMotorMotionMagicAtTarget);

        inputs.pivotMotorSupplyCurrent = this.pivotMotorSupplyCurrent.getValue();
        inputs.pivotMotorStatorCurrent = this.pivotMotorStatorCurrent.getValue();
        inputs.pivotMotorPosition = this.pivotMotorPosition.getValue();
        inputs.pivotMotorVoltage = this.pivotMotorVoltage.getValue();
        inputs.pivotMotorMotionMagicAtTarget = this.pivotMotorMotionMagicAtTarget.getValue();

        return refreshStatusCode;
    }

    @Override
    public StatusCode applyOutputs(ClawOutputs outputs) {
        return switch (outputs.gripperOutputMode) {
            case IDLE -> {
                gripperMotor.stopMotor(); 
                yield StatusCode.OK;
            }
            case VOLTAGE -> gripperMotor.setControl(voltageController.withOutput(outputs.gripperTargetVoltage));
            case MOTION_MAGIC -> gripperMotor.setControl(motionProfileController.withPosition(outputs.gripperProfileSetpoint));
        };
    }
}
