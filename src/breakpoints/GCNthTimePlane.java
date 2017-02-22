package breakpoints;

import entities.Device;

public class GCNthTimePlane extends BreakpointBase {
	private int mChipIndex;
	private int mPlaneIndex;
	private int mValue;
	
	public GCNthTimePlane() {
		super();
	}
	
	@Override
	public boolean breakpointHit(Device<?, ?, ?, ?> previousDevice, Device<?, ?, ?, ?> currentDevice) {
		if (previousDevice == null) {
			return false;
		}

		int prevGCCount = previousDevice.getChip(this.mChipIndex).getPlane(this.mPlaneIndex).getGCExecutions();
		int currGCCount = currentDevice.getChip(this.mChipIndex).getPlane(this.mPlaneIndex).getGCExecutions();

		return (prevGCCount != this.mValue) && (currGCCount == this.mValue);
	}
	
	public int getValue() {
		return mValue;
	}

	public void setValue(int value) throws Exception {
		if (!BreakpointsConstraints.isGCCountLegal(value)) {
			throw BreakpointsConstraints.reportSetterException(SetterError.ILLEGAL_COUNT);
		}
		
		mValue = value;
	}

	public int getPlaneIndex() {
		return mPlaneIndex;
	}

	public void setPlaneIndex(int planeIndex) throws Exception {
		if (!BreakpointsConstraints.isPlaneIndexLegal(planeIndex)) {
			throw BreakpointsConstraints.reportSetterException(SetterError.ILLEGAL_PLANE);
		}
		
		mPlaneIndex = planeIndex;
	}
	
	public int getChipIndex() {
		return mChipIndex;
	}

	public void setChipIndex(int chipIndex) throws Exception {
		if (!BreakpointsConstraints.isChipIndexLegal(chipIndex)) {
			throw BreakpointsConstraints.reportSetterException(SetterError.ILLEGAL_CHIP);
		}
		
		mChipIndex = chipIndex;
	}

	@Override
	public String getDisplayName() {
		return "Invoke garbage collection in the i-th time in plane P";
	}

	@Override
	public String getDescription() {
		return "Number of garbage collection invocations is " + getValue() + " in plane (<chip,plane>): "
				+ "<" 
				+ mChipIndex + ","
				+ mPlaneIndex
				+ ">";
	}

	@Override
	public void addComponents() {
		mComponents.add(new BreakpointComponent("chipIndex", int.class, "Chip"));
		mComponents.add(new BreakpointComponent("planeIndex", int.class, "Plane"));
		mComponents.add(new BreakpointComponent("value", int.class, "Value"));
	}

	@Override
	public boolean isEquals(IBreakpoint other) {
		if (!(other instanceof GCNthTimePlane)) return false; 
		GCNthTimePlane otherCasted = (GCNthTimePlane) other;
		
		return mPlaneIndex == otherCasted.getPlaneIndex()
				&& mValue == otherCasted.getValue()
				&& mChipIndex == otherCasted.getChipIndex();
	}
	
	@Override
	public String getHitDescription() {
		return "Number of garbage collection invocations in plane (<chip,plane>): "
				+ "<" 
				+ mChipIndex + ","
				+ mPlaneIndex
				+ "> reached " + getValue();
	}
}
