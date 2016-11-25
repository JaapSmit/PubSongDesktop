public class AfspeellijstData {
	private long id;
	private Nummer nummer;
	private int votes;
	private boolean adminVote;
	private boolean playing;
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public Nummer getNummer() {
		return nummer;
	}
	public void setNummer(Nummer nummer) {
		this.nummer = nummer;
	}
	public int getVotes() {
		return votes;
	}
	public void setVotes(int votes) {
		this.votes = votes;
	}
	public boolean isAdminVote() {
		return adminVote;
	}
	public void setAdminVote(boolean adminVote) {
		this.adminVote = adminVote;
	}
	public boolean isPlaying() {
		return playing;
	}
	public void setPlaying(boolean playing) {
		this.playing = playing;
	}
		
	
}
