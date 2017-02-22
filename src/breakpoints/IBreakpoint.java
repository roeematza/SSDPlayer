package breakpoints;

import java.util.List;

import org.w3c.dom.Element;

import entities.Device;
import manager.SSDManager;

public interface IBreakpoint {
	public boolean breakpointHit(Device<?, ?, ?, ?> previousDevice, Device<?, ?, ?, ?> currentDevice);
	
	public boolean isHit();

	public void setIsHit(boolean value);

	public void readXml(Element xmlElement) throws Exception;
	
	public String getDisplayName();
	
	public String getDescription();
	
	public String getHitDescription();

	public void addComponents();

	public List<BreakpointComponent> getComponents();
	
	public boolean isEquals(IBreakpoint other);
	
	public boolean isManagerSupported(SSDManager<?, ?, ?, ?, ?> manager);

	public boolean isActive();

	public void setIsActive(boolean paramBoolean);
}
