package api.v1.model;

/**
 * 
 * @author kennethlyon
 */
public class Type {
	private int id;

    public Type(){
        this.id=-9;
    }

    public Type(int id){
		this.id=id;
	}

	public int getId(){
		return id;
	}
}
