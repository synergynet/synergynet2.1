package synergyspace.gestures;

import org.ximtec.igesture.core.Descriptor;

public class GestureDescriptor implements Descriptor {
	
	private static final long serialVersionUID = -4400792315330613492L;
	private String uid;
	
	public Class<? extends Descriptor> getType() {
		return this.getClass();
	}

	public String getID() {
		if(this.uid == null) {
			this.uid = org.safehaus.uuid.UUIDGenerator.getInstance()
			.generateRandomBasedUUID().toString();	}
		return uid;
	}

	public void setID(String id) {
		this.uid = id;
	}
}
