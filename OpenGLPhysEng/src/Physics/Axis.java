package Physics;

import Math.Mat3d;
import Math.MathsSoup;
import Math.Quaternion;
import Math.Vect3d;

public class Axis implements PhysicsAttrib{
	
	//Centre of Mass.
	Vect3d centre;
	
	//Orientation.
	Quaternion orientation;
	Quaternion _orientation = new Quaternion();
	private double orientationTolerance = 0.001;
	
	//Basis.
	private Mat3d B;
	private Mat3d _B = new Mat3d();
	
	//Moment of Inertia.
	private Mat3d I = new Mat3d();
	private Mat3d _I = new Mat3d();
	
	//Angular Variables.
	public Vect3d angMomentum = new Vect3d();
	public Vect3d w = new Vect3d();
	public Vect3d torque = new Vect3d();
	
	//Angular Energy.
	private double correctE = 0;
	private double actualE = 0;
	
	//Temp variables for calculations and avoid object creation.
	MathsSoup temps = new MathsSoup();
	
	public Axis(Vect3d centre, Quaternion orientation, Mat3d Basis, Mat3d MomentOfInertia) {
		this.centre = centre;
		this.orientation = orientation;
		orientation.getCongecate(_orientation);
		
		B = Basis;
		B.inverse(_B);
		I = MomentOfInertia;
		I.inverse(_I);
	}
	
	public void update(double dt) {
		temps.startOfMethod();
		//////////////////////
		Vect3d L = getApproxAngularMomentum(temps.getVect());
		actualE = Vect3d.dot(L,w)/2;
		////////////////////
		temps.endOfMethod();
		
		if(torque.lengthSq() > 0) correctE = actualE;
		
		if(actualE > correctE) update6(dt);
		else update3(dt);
		
		if(Math.abs(1 - orientation.lengthSq()) > orientationTolerance*orientationTolerance) orientation.normalise();

	}
	
	private void update3(double dt) {
		temps.startOfMethod();
		/////////////////////
		
		rotateBasis(dt);
		
		Vect3d.increment(angMomentum, torque, dt);
		
		//_Ib = B_I_B
		Mat3d _Ib = getInvMomentOfInertia(temps.getMat());
		
		//w = _Ib L 
		Mat3d.mult(w, _Ib, angMomentum);
		
		///////////////////
		temps.endOfMethod();
	}
	private void update4(double dt) {
		temps.startOfMethod();
		//////////////////////
		
		rotateBasis(dt);
		
		Mat3d _Ib = this.getInvMomentOfInertia(temps.getMat());
		Mat3d Ib = this.getMomentOfInertia(temps.getMat());
		
		Vect3d L =  Mat3d.mult(temps.getVect(), Ib, w);
		Vect3d dw = temps.getVect();
		torque.copyTo(dw);
		Vect3d.crossIncrement(dw, L, w, 1);
		Mat3d.mult(dw, _Ib, dw);
		
		Vect3d.increment(w, dw, dt);
		
		////////////////////
		temps.endOfMethod();
	}
	
	
	public Vect3d k = new Vect3d();
	private void update6(double dt) {
		temps.startOfMethod();
		//////////////////////
		
		Mat3d _Ib = getInvMomentOfInertia(temps.getMat());
		
		// L += T;
		Vect3d L = angMomentum;
		Vect3d.increment(L, torque, dt);
		
		//M1 = _I * X_L * dt
		Mat3d M1 = temps.getMat();
		Mat3d crossL = temps.getMat();
		Mat3d.crossTransform(crossL, L, Mat3d.I);
		Mat3d.mult(M1, _Ib, crossL);
		Mat3d.scale(M1, M1, dt);
		
		//M2 = X_k * dt
		Mat3d M2 = temps.getMat();
		k = Mat3d.mult(temps.getVect(), _Ib, L);
		Mat3d.crossTransform(M2, k, Mat3d.I);
		Mat3d.scale(M2, M2, dt);
		
		//M = _(I - M1 + M2)
		Mat3d M = temps.getMat();
		Mat3d.I.copyTo(M);
		Mat3d.sub(M, M, M1);
		Mat3d.add(M, M, M2);
		M.inverse(M);
		
		//w = M * k
		Mat3d.mult(w, M, k);
		
		rotateBasis(dt);
		
		////////////////////
		temps.endOfMethod();
	}
	
	public Vect3d getApproxAngularMomentum(Vect3d result) {
		temps.startOfMethod();
		//////////////////////
		Vect3d L = Mat3d.mult(result, getMomentOfInertia(temps.getMat()), w);
		////////////////////
		temps.endOfMethod();
		return L;
	}
	
	public void addTorque(Vect3d force, Vect3d location) {
		temps.startOfMethod();
		////////////////////
		Vect3d disp = temps.getVect();
		Vect3d.sub(disp, location, centre);
		Vect3d.crossIncrement(torque, disp, force, 1);
		////////////////////
		temps.endOfMethod();
	}
	
	public Mat3d getMomentOfInertia(Mat3d result) {
		temps.startOfMethod();
		//////////////////////
		Mat3d tB = temps.getMat();
		Mat3d _tB = temps.getMat();
		
		Mat3d.rotate(tB, orientation, B, _orientation);
		Mat3d.rotate(_tB, _orientation, _B, orientation);
		
		Mat3d.mult(result, I, _tB);
		Mat3d.mult(result, tB, result);
		
		////////////////////
		temps.endOfMethod();
		return result;
	}
	
	public Mat3d getInvMomentOfInertia(Mat3d result) {
		temps.startOfMethod();
		//////////////////////
		Mat3d tB = temps.getMat();
		Mat3d _tB = temps.getMat();
		
		Mat3d.rotate(tB, orientation, B, _orientation);
		Mat3d.rotate(_tB, _orientation, _B, orientation);
		
		Mat3d.mult(result, _I, _tB);
		Mat3d.mult(result, tB, result);
		
		////////////////////
		temps.endOfMethod();
		
		return result;
	}
	
	public void setAngVel(double x, double y, double z) {
		temps.startOfMethod();
		//////////////////////
		
		w.set(x,y,z);
		
		Mat3d Ib = temps.getMat();
		
		Ib = getMomentOfInertia(Ib);
		Mat3d.mult(angMomentum, Ib, w);
		
		correctE = Vect3d.dot(angMomentum, w)/2;
		actualE = correctE;
		////////////////////
		temps.endOfMethod();
	}
	
	public Vect3d getAngVel(Vect3d result) {
		w.copyTo(result);
		return result;
	}
	public Vect3d getVel(Vect3d result, Vect3d pos) {
		temps.startOfMethod();
		//////////////////////
		Vect3d difference = temps.getVect();
		
		Vect3d.sub(difference, pos, centre);
		Vect3d.cross(result, w, difference);
		
		////////////////////
		temps.endOfMethod();
		return result;
	}
	
	public double getRotationalEnergy() {
		return correctE;
	}
	
	public double getApproxE() {
		return actualE;
	}
	
	private void rotateBasis(double dt) {
		temps.startOfMethod();
		//////////////////////
		
		//rotateTest(B, dt);
		double angle = Math.sqrt(w.lengthSq());
		if(angle == 0)
			return;
		double sin = Math.sin(angle*dt);
		double cos = Math.cos(angle*dt);
		
		Vect3d norm = temps.getVect();
		Vect3d.scale(norm, w, 1d/angle);
		
		Mat3d R = temps.getMat();
		Mat3d.I.copyTo(R);
		
	
		R.rotate(norm, cos, sin);
		Mat3d.mult(B, R, B);
		
		//Calc Inverse.
//		Mat3d.I.copyTo(R);
//		R.rotate(norm, cos, -sin);
//		
//		Mat3d.mult(_B, _B, R);
		B.c1.normalize();
		B.c2.normalize();
		B.c3.normalize();
		B.inverse(_B);
		
		////////////////////
		temps.endOfMethod();
	}
	
	private void rotateOrientation(double dt) {
		temps.startOfMethod();
		//////////////////////
		
		Quaternion w = temps.getQuaternion();
		w.set(w);
		
		double wLength = Math.sqrt(this.w.lengthSq());
		Vect3d wN = temps.getVect();
		Vect3d.scale(wN, this.w, 1d/wLength);
		
		Quaternion dq = Quaternion.getRotation(temps.getQuaternion(), wLength*dt, wN);
		
		Quaternion.mult(orientation, dq, orientation);
		orientation.normalise();
		
		orientation.getCongecate(_orientation);
		
		////////////////////
		temps.endOfMethod();
	}
	
	private void rotateOrientation2(double dt) {
		temps.startOfMethod();
		//////////////////////
		
		Quaternion w = temps.getQuaternion();
		w.set(this.w);
		
		Quaternion dq = Quaternion.mult(temps.getQuaternion(), w, orientation);
		Quaternion.scale(dq, dq, dt/2);
		
		Quaternion.add(orientation, dq, orientation);
		//orientation.normalise();
		
		orientation.getCongecate(_orientation);
		
		///////////////////////
		temps.endOfMethod();
	}
	
	//Small angle sin(wdt) =~ wdt?
	private void rotateTest(Mat3d basis, double dt) {
		temps.startOfMethod();
		//////////////////////
		
		Vect3d w = Vect3d.scale(temps.getVect(), this.w, dt);
		Vect3d cross = temps.getVect();
		Vect3d P = temps.getVect();
		Vect3d W = temps.getVect();
		double wSq = w.lengthSq();
		double dot = Vect3d.dot(w, basis.c1);
		
		Vect3d.scale(P, basis.c1, 1 - wSq/2d);
		Vect3d.scale(W, w, dot/2);
		Vect3d.cross(cross, w, basis.c1);
		
		Vect3d.add(basis.c1, P, W);
		Vect3d.add(basis.c1, basis.c1, cross);
		/////////
		dot = Vect3d.dot(w, basis.c2);
		
		Vect3d.scale(P, basis.c2, 1 - wSq/2d);
		Vect3d.scale(W, w, dot/2);
		Vect3d.cross(cross, w, basis.c2);
		
		Vect3d.add(basis.c2, P, W);
		Vect3d.add(basis.c2, basis.c2, cross);
		////////
		dot = Vect3d.dot(w, basis.c3);
		
		Vect3d.scale(P, basis.c3, 1 - wSq/2d);
		Vect3d.scale(W, w, dot/2);
		Vect3d.cross(cross, w, basis.c3);
		
		Vect3d.add(basis.c3, P, W);
		Vect3d.add(basis.c3, basis.c3, cross);
		
		////////////////////
		temps.endOfMethod();
	}

	@Override
	public void resetForces() {
		torque.set(0,0,0);
		
	}
}	

//	private Mat3d Ib = new Mat3d();
//	private Mat3d _Ib = new Mat3d();
//	private Mat3d dIb = new Mat3d();
//	private Vect3d dw = new Vect3d();
//	private void update2(double dt) {
//		
//		rotateBasis(dt);
//		//Ib = BI_B, change of basis.
//		Mat3d.mult(Ib, I, _B);
//		Mat3d.mult(Ib, B, Ib);
//		
//		//_Ib = B_I_B
//		Mat3d.mult(_Ib, _I, _B);
//		Mat3d.mult(_Ib, B, _Ib);
//		
//		
//		//dIb/dt = XwIb - IbXw
//		//But (IbXw)w = 0.
//		Mat3d.crossTransform(dIb, w, Ib);
//		
//		
//		//dw/dt = _Ib(T - (dIb/dt)w)
//		Mat3d.mult(dw, dIb, w);
//		Vect3d.sub(dw, torque, dw);
//		Mat3d.mult(dw, _Ib, dw);
//		
//		Vect3d.increment(w, dw, dt);
//	}
	
//	public void update5(double dt) {
//		temps.startOfMethod();
//		//////////////////////
//		
//		rotateBasis(dt);
//		
//		Mat3d Ib = getMomentOfInertia(temps.getMat());
//		Mat3d _Ib = getInvMomentOfInertia(temps.getMat());
//		Mat3d Inv = temps.getMat();
//		
//		Vect3d dw = Vect3d.scale(temps.getVect(), torque, dt);
//		dw = Mat3d.mult(dw, _Ib, dw);
//		
//		Vect3d.increment(angMomentum, torque, dt);
//		Vect3d L = Mat3d.mult(temps.getVect(), Ib, w);
//		L = angMomentum;
//		
//		Mat3d.crossTransform(Inv, L, Mat3d.I);
//		Mat3d.mult(Inv, _Ib, Inv);
//		Mat3d.scale(Inv, Inv, -dt);
//		Mat3d.add(Inv, Mat3d.I, Inv);
//		Inv.inverse(Inv);
//		
//		Vect3d.add(w, w, dw);
//		Mat3d.mult(w, Inv, w);
//		
//		//rotateBasis(dt/2);
//		
//		//rotateOrientation(dt);
//		
//		////////////////////
//		temps.endOfMethod();
//	}


