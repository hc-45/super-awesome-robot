/**************** PROJECT SUPER AWESOME ROBOT *****************/
/* Copyright (c) 2026 StuyPulse Robotics. All rights reserved.*/
/* This work is licensed under the terms of the MIT license.  */
/**************************************************************/
package com.stuypulse.robot.subsystems.elevator;

import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorConfigs;
import com.stuypulse.robot.subsystems.elevator.ElevatorConstants.ElevatorSettings;

import org.wpilib.units.measure.Angle;
import org.wpilib.units.measure.Current;
import org.wpilib.units.measure.Voltage;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.MotionMagicTorqueCurrentFOC;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;

// intentioanlly package private
sealed abstract class ElevatorIOBase implements ElevatorIO permits ElevatorIOSim, ElevatorIOTalonFX {
    private final TalonFX TRMotor; // top right (leader)
    private final TalonFX BRMotor; // bottom right
    private final TalonFX BLMotor; // bottom left
    private final TalonFX TLMotor; // top left

    private final MotionMagicTorqueCurrentFOC motionProfileController;
    private final Follower rightFollowerController;
    private final Follower leftFollowerController;

    private final StatusSignal<Current> TRMotorSupplyCurrent;
    private final StatusSignal<Current> TRMotorStatorCurrent;
    private final StatusSignal<Angle> TRMotorPosition;
    private final StatusSignal<Boolean> TRMotorMotionMagicAtTarget;
    private final StatusSignal<Voltage> TRMotorVoltage;

    protected ElevatorIOBase(final TalonFX TRMotor, final TalonFX BRMotor, final TalonFX BLMotor, final TalonFX TLMotor) {
        this.TRMotor = TRMotor;
        this.BRMotor = BRMotor;
        this.BLMotor = BLMotor;
        this.TLMotor = TLMotor;

        ElevatorConfigs.ELEVATOR_GEARBOX_MOTOR_CONFIG.configure(this.TRMotor);
        ElevatorConfigs.ELEVATOR_GEARBOX_MOTOR_CONFIG.configure(this.BRMotor);
        ElevatorConfigs.ELEVATOR_GEARBOX_MOTOR_CONFIG.configure(this.BLMotor);
        ElevatorConfigs.ELEVATOR_GEARBOX_MOTOR_CONFIG.configure(this.TLMotor);

        this.motionProfileController = new MotionMagicTorqueCurrentFOC(ElevatorSettings.STOWED_ANGLE);
        this.rightFollowerController = new Follower(this.TRMotor.getDeviceID(), MotorAlignmentValue.Aligned);
        this.leftFollowerController = new Follower(this.TRMotor.getDeviceID(), MotorAlignmentValue.Aligned);

        this.BRMotor.setControl(rightFollowerController);
        this.TLMotor.setControl(leftFollowerController);
        this.BLMotor.setControl(leftFollowerController);

        this.TRMotorSupplyCurrent = this.TRMotor.getSupplyCurrent();
        this.TRMotorStatorCurrent = this.TRMotor.getStatorCurrent();
        this.TRMotorPosition = this.TRMotor.getPosition();
        this.TRMotorMotionMagicAtTarget = this.TRMotor.getMotionMagicAtTarget();
        this.TRMotorVoltage = this.TRMotor.getMotorVoltage();
    }

    @Override
    public StatusCode updateInputs(final ElevatorInputs inputs) {
        final StatusCode refreshStatusCode = BaseStatusSignal.refreshAll(TRMotorSupplyCurrent, TRMotorStatorCurrent, TRMotorPosition, TRMotorMotionMagicAtTarget, TRMotorVoltage);

        inputs.TRMotorSupplyCurrent = TRMotorSupplyCurrent.getValue();
        inputs.TRMotorStatorCurrent = TRMotorStatorCurrent.getValue();
        inputs.TRMotorPosition = TRMotorPosition.getValue();
        inputs.TRMotorMotionMagicAtTarget = TRMotorMotionMagicAtTarget.getValue();
        inputs.TRMotorVoltage = TRMotorVoltage.getValue();

        return refreshStatusCode;
    }

    @Override
    public StatusCode applyOutputs(final ElevatorOutputs outputs) {
        return switch (outputs.outputMode) {
            case IDLE -> {
                this.TRMotor.stopMotor();

                this.BRMotor.setControl(rightFollowerController);
                this.TLMotor.setControl(leftFollowerController);
                this.BLMotor.setControl(leftFollowerController);

                yield StatusCode.OK;
            }
            case MOTION_MAGIC -> this.TRMotor.setControl(motionProfileController.withPosition(outputs.profileSetpoint));
        };
    }
}
