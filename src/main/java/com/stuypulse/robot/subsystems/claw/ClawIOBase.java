/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.claw;

import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawConfigs;
import com.stuypulse.robot.subsystems.claw.ClawConstants.ClawSettings;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.DutyCycleOut;
import com.ctre.phoenix6.controls.MotionMagicVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

// intentionally package private
sealed abstract class ClawIOBase implements ClawIO permits ClawIOSim, ClawIOTalonFX {
    private final TalonFX rollerMotor;
    private final TalonFX pivotMotor;

    private final MotionMagicVoltage motionProfileController;
    private final DutyCycleOut rollerDutyCycleController;

    private final StatusSignal<Current> pivotMotorSupplyCurrent;
    private final StatusSignal<Current> pivotMotorStatorCurrent;
    private final StatusSignal<Angle> pivotMotorPosition;
    private final StatusSignal<Boolean> pivotMotorMotionMagicAtTarget;
    private final StatusSignal<Voltage> pivotMotorVoltage;

    private final StatusSignal<Current> rollerMotorSupplyCurrent;
    private final StatusSignal<Current> rollerMotorStatorCurrent;
    private final StatusSignal<Temperature> rollerMotorTemperature;
    private final StatusSignal<AngularVelocity> rollerMotorAngularVelocity;
    private final StatusSignal<Voltage> rollerMotorVoltage;

    protected ClawIOBase(final TalonFX rollerMotor, final TalonFX pivotMotor) {
        this.rollerMotor = rollerMotor;
        this.pivotMotor = pivotMotor;

        ClawConfigs.CLAW_ROLLER_MOTOR_CONFIG.configure(this.rollerMotor);
        ClawConfigs.CLAW_PIVOT_MOTOR_CONFIG.configure(this.pivotMotor);

        this.motionProfileController = new MotionMagicVoltage(ClawSettings.Pivot.INTAKE_ANGLE).withEnableFOC(true);
        this.rollerDutyCycleController = new DutyCycleOut(ClawSettings.Rollers.IDLE_DUTY_CYCLE).withEnableFOC(true);

        // pivot signals
        this.pivotMotorSupplyCurrent = this.pivotMotor.getSupplyCurrent();
        this.pivotMotorStatorCurrent = this.pivotMotor.getStatorCurrent();
        this.pivotMotorPosition = this.pivotMotor.getPosition();
        this.pivotMotorVoltage = this.pivotMotor.getMotorVoltage();
        this.pivotMotorMotionMagicAtTarget = this.pivotMotor.getMotionMagicAtTarget();

        // roller signals
        this.rollerMotorSupplyCurrent = this.rollerMotor.getSupplyCurrent();
        this.rollerMotorStatorCurrent = this.rollerMotor.getStatorCurrent();
        this.rollerMotorTemperature = this.rollerMotor.getDeviceTemp();
        this.rollerMotorAngularVelocity = this.rollerMotor.getVelocity();
        this.rollerMotorVoltage = this.rollerMotor.getMotorVoltage();
    }

    @Override
    public StatusCode updateInputs(final ClawInputs inputs) {
        final StatusCode refreshStatusCode = BaseStatusSignal.refreshAll(pivotMotorSupplyCurrent, pivotMotorStatorCurrent, pivotMotorPosition, pivotMotorVoltage, pivotMotorMotionMagicAtTarget);

        inputs.pivotMotorSupplyCurrent = this.pivotMotorSupplyCurrent.getValue();
        inputs.pivotMotorStatorCurrent = this.pivotMotorStatorCurrent.getValue();
        inputs.pivotMotorPosition = this.pivotMotorPosition.getValue();
        inputs.pivotMotorVoltage = this.pivotMotorVoltage.getValue();
        inputs.pivotMotorMotionMagicAtTarget = this.pivotMotorMotionMagicAtTarget.getValue();

        inputs.rollerMotorSupplyCurrent = this.rollerMotorSupplyCurrent.getValue();
        inputs.rollerMotorStatorCurrent = this.rollerMotorStatorCurrent.getValue();
        inputs.rollerMotorTemperature = this.rollerMotorTemperature.getValue();
        inputs.rollerMotorAngularVelocity = this.rollerMotorAngularVelocity.getValue();
        inputs.rollerMotorVoltage = this.rollerMotorVoltage.getValue();
        return refreshStatusCode;
    }

    @Override
    public StatusCode applyPivotOutputs(final PivotOutputs outputs) {
        return switch (outputs.pivotOutputMode) {
            case IDLE -> {
                pivotMotor.stopMotor();
                yield StatusCode.OK;
            }
            case MOTION_MAGIC -> pivotMotor.setControl(motionProfileController.withPosition(outputs.pivotProfileSetpoint));
        };
    }

    @Override
    public StatusCode applyRollerOutputs(final RollerOutputs outputs) {
        return switch (outputs.rollerOutputMode) {
            case IDLE -> {
                rollerMotor.stopMotor();
                yield StatusCode.OK;
            }
            case DUTY_CYCLE -> rollerMotor.setControl(rollerDutyCycleController.withOutput(outputs.rollerTargetDutyCycle));
        };
    }
}
