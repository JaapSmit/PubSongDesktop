public class Nummer {
	private String artiest;
	private String titel;
	private long id;
	
	public Nummer() {
	}
	
	public Nummer(String artiest, String titel){
		this.artiest = artiest;
		this.titel = titel;
	}

	public String getArtiest() {
		return artiest;
	}

	public void setArtiest(String artiest) {
		this.artiest = artiest;
	}

	public String getTitel() {
		return titel;
	}

	public void setTitel(String titel) {
		this.titel = titel;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	
	
	
}
