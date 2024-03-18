package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class PivotSubsystem extends PositionableSubsystem {
  private final CANSparkMax pivot;
  private final AbsoluteEncoder pivotEncoder;
  private static PivotSubsystem self;


  //todo refactor this speed control later into base class or separate class, no time to update base class for now

  //Safe zones for speed control . Absolute encoder values
  private final double minpos = 5;
  private final double maxpos = 150;
  private final double midpos = (maxpos - minpos)/2;
  private final double downwardslowestposition = (midpos - minpos)/2;
  private final double upwardslowestposition = midpos+(maxpos - midpos)/2;

  //speed control for delta of move positions (absolute encoders)
  private final double maxspeeddelta = 80;
  private final double lowestspeeddelta = 30;

  //speeds
  private final double slowestspeed = 0.1;
  private final double mediumspeed = 0.2;

  enum PivotDirection {
    UP,
    DOWN,
    NONE
  };
  private static final int MotorDirectionForUP = -1;

  public PivotSubsystem() {
    pivot = new CANSparkMax(Constants.ShooterConstants.shooterPivotCanId, MotorType.kBrushless);
    pivotEncoder = pivot.getAbsoluteEncoder();
    pivotEncoder.setPositionConversionFactor(1000);
    pivot.setIdleMode(IdleMode.kBrake);
    pivot.setSmartCurrentLimit(Constants.IntakeConstants.CURRENT_LIMIT_A); // gives a limit for how much power, the motor can receive

    super.init(pivot);
    super.hasAbsEncoder(true);
    super.setMaxSpeed(0.3);
    //super.setMinPoint(0);
    //super.setRange(255);  
  }

  public static PivotSubsystem getInstance() {
    return self==null ? self = new PivotSubsystem() : self;
  }

  public void move(double speed) {
    // setCurrentSpeed(limitValue(speed, Constants.IntakeConstants.MAX_SPEED));
    //System.out.println("Move called with speed "+speed);

    restrictSpeed(speed);
  }

  @Override
  public void moveToPosition(long requiredPosition) {
    PivotDirection directionNeeded = pivotEncoder.getPosition() > requiredPosition ? PivotDirection.DOWN : PivotDirection.UP;
    //safeguard against recycle of encoder
    if (pivotEncoder.getPosition() > 900) {
      directionNeeded = PivotDirection.UP;
    }
    int directionsign = (directionNeeded == PivotDirection.UP) ? MotorDirectionForUP : -1*MotorDirectionForUP;

    

    double speed = getDeltaSpeedRestriction(directionsign*getMaxSpeed(), requiredPosition);
    move(speed);
  }

  public void stop() {
    //System.out.println("stop called in command");
    move(0);
  }

  public PivotDirection getDirection(double speed) {
    int directionsign = (int)Math.signum(speed);
    switch (directionsign) {
      case MotorDirectionForUP:
        return PivotDirection.UP;
      case -1*MotorDirectionForUP:
        return PivotDirection.DOWN;
      default:
        return PivotDirection.NONE;
    }
  }

  //gives degrees of current pivot absolute encoder position
  public double getPositionDegrees(){
    return getAngleForGivenPosition(pivotEncoder.getPosition());
  }

  //given an angle, gives the absolute encoder value 
  public double getEncoderWithAngle(double angle){
    return (angle/360)*1000;
  }

  public double getAngleForGivenPosition(double encoderPosition) {
    return (encoderPosition/1000) * 360;
  }

  //Safe guard against max and min positions of robot
  public void restrictSpeed(double speed) {
    PivotDirection speedDirection = getDirection(speed);
    double currentposition = pivotEncoder.getPosition();

    if(speedDirection == PivotDirection.UP) {
       //stop if encoder has reached max
      if (currentposition >= maxpos && currentposition <= 900){ //<=900 is used to take care of encoder recycle
        speed = 0.0;
      } else if (currentposition >= upwardslowestposition) {
        speed = MotorDirectionForUP * slowestspeed ;
      } else if (currentposition >= midpos) {
        speed = MotorDirectionForUP * mediumspeed;
      }
    }

    if (speedDirection == PivotDirection.DOWN) {
      //restrict min position
      if (currentposition <= minpos || currentposition >= 900){ //>=900 is used to take care of encoder recycle
        speed = 0.0;
      } else if (currentposition <= downwardslowestposition) {
        speed = -1.0 * MotorDirectionForUP * slowestspeed;
      } else if (currentposition <= midpos) {
        speed = -1.0 * MotorDirectionForUP * mediumspeed;
      }
    }
    
    double maxSpeed = getMaxSpeed();
    speed = MathUtil.clamp(speed, -maxSpeed, maxSpeed);


    pivot.set(speed);
  }

  //reduce speed when closer to target position
  public double getDeltaSpeedRestriction(double speed, double targetPosition) {
    double delta = Math.abs(targetPosition - pivotEncoder.getPosition());
    double signum = Math.signum(speed);

    if ( delta >= maxspeeddelta) {
      return speed;
    } 

    if (delta <= lowestspeeddelta) {
      return signum * slowestspeed;
    }

    return signum * mediumspeed;
  }

  public double getMaxAngle() {
    return getAngleForGivenPosition(maxpos);
  }

  public double getMinAngle() {
    return getAngleForGivenPosition(minpos);
  }
 
  public void periodic(){
    //SmartDashboard.putNumber("PivotEncoder", pivotEncoder.getPosition());
    //restrictSpeed(pivot.get());
    super.periodic();
  }
}