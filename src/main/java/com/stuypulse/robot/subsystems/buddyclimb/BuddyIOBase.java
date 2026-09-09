/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.buddyclimb;

import com.stuypulse.robot.subsystems.buddyclimb.BuddyClimbConstants.BuddyClimbConfigs;
import com.stuypulse.robot.subsystems.buddyclimb.BuddyClimbConstants.BuddyClimbSettings;

import org.wpilib.units.measure.*;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

// intentionally package private
sealed abstract class BuddyIOBase implements BuddyIO permits BuddyIOSim, BuddyIOTalonFX {
    private final TalonFX winchMotor;

    private final PositionVoltage positionController;

    private final StatusSignal<Current> winchMotorSupplyCurrent;
    private final StatusSignal<Current> winchMotorStatorCurrent;
    private final StatusSignal<Angle> winchMotorPosition;
    private final StatusSignal<Voltage> winchMotorVoltage;

    protected BuddyIOBase(final TalonFX winchMotor) {
        this.winchMotor = winchMotor;

        BuddyClimbConfigs.WINCH_MOTOR_CONFIG.configure(this.winchMotor);

        this.positionController = new PositionVoltage(BuddyClimbSettings.HELD_ANGLE).withEnableFOC(true);

        this.winchMotorSupplyCurrent = this.winchMotor.getSupplyCurrent();
        this.winchMotorStatorCurrent = this.winchMotor.getStatorCurrent();
        this.winchMotorPosition = this.winchMotor.getPosition();
        this.winchMotorVoltage = this.winchMotor.getMotorVoltage();
    }

    @Override
    public StatusCode updateInputs(final BuddyClimbInputs inputs) {
        final StatusCode refreshStatusCode = BaseStatusSignal.refreshAll(winchMotorSupplyCurrent, winchMotorStatorCurrent, winchMotorPosition, winchMotorVoltage);

        inputs.winchMotorSupplyCurrent = this.winchMotorSupplyCurrent.getValue();
        inputs.winchMotorStatorCurrent = this.winchMotorStatorCurrent.getValue();
        inputs.winchMotorPosition = this.winchMotorPosition.getValue();
        inputs.winchMotorVoltage = this.winchMotorVoltage.getValue();

        return refreshStatusCode;
    }

    @Override
    public StatusCode applyOutputs(final BuddyClimbOutputs outputs) {
        return switch (outputs.outputMode) {
            case STOP -> {
                this.winchMotor.stopMotor();
                yield StatusCode.OK;
            }
            case POSITION_VOLTAGE -> this.winchMotor.setControl(positionController.withPosition(outputs.targetPosition));
        };
    }
}
