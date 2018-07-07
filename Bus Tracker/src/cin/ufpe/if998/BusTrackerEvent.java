package cin.ufpe.if998;

public class BusTrackerEvent {
	/** N�mero global do ve�culo. */
    private long unidade;
    
    /**Nome da empresa (anonimizado). */
    private String nome;
    
    /** Numero do ve�culo na empresa (�s vezes � o mesmo valor de Unidade). */
    private int matricula;
    
    /**
    Timestamp da leitura da posi��o */
    
    private String timestamp;
    
    /** Latitude */
    private double latitude;
    
    /** Longitude */
    private double longitude;
    
    public BusTrackerEvent(long unidade, String nome, int matricula, String timestamp, double latitude, double longitude) {
    	
    	this.unidade= unidade;
    	this.nome= nome;
    	this.matricula= matricula;
    	this.timestamp= timestamp; 
    	this.latitude= latitude; 
    	this.longitude= longitude;
        
    }

    public long getUnidade() {
		return unidade;
	}

	public String getNome() {
		return nome;
	}

	public int getMatricula() {
		return matricula;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	
    
   
    
}
