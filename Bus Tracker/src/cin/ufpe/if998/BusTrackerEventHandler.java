package cin.ufpe.if998;


import cin.ufpe.rabbitmq.Recv;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import org.json.JSONObject;


public class BusTrackerEventHandler {
	
	public static void main(String[] args) throws Exception {
		String reciveCompany;
		long reciveMatricula;
		EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();
		engine.getEPAdministrator().getConfiguration().addEventType(BusTrackerEvent.class);
        
		//selecionar os nomes das empresas
		//String eplc = "select nome from BusTrackerEvent";
		//selecionar os onibus das empresas
		
		reciveCompany = "E1";
		reciveMatricula = 1626;
		
		//consultas epl
		//String eplb = "select matricula from BusTrackerEvent where nome="+reciveCompany;
		
		String epla = String.format("select timestamp,latitude,longitude from BusTrackerEvent#time( 10 sec) where matricula=%d and nome='%s'", reciveMatricula,reciveCompany);
		
		//tratamento
		EPStatement statement = engine.getEPAdministrator().createEPL(epla);
		
		statement.addListener((newData, oldData) -> {
			String timestamp = (String) newData[0].get("timestamp");
			double latitude = (double) newData[0].get("latitude");
			double longitude = (double) newData[0].get("longitude");
			System.out.println(String.format("O trace é: %s, com a latitude: %f e com a longitude: %f.",timestamp,latitude,longitude));
			//System.out.println("A temperatura media nos ultimos 100 eventos é: "+ temperatureMedia+" °C.\n");
			
		});

		Recv recieve = new Recv();

		for(JSONObject rec : recieve.getData()){
			System.out.println(rec);
		}

//		engine.getEPRuntime().sendEvent(new BusTrackerEvent(3333,"E1",3333,"2018-01-20 00:39:23.670",29.3827,91.15103));
//		engine.getEPRuntime().sendEvent(new BusTrackerEvent(3333,"E1",3333,"2018-01-20 04:48:48.073",29.3825,91.15103));
//		engine.getEPRuntime().sendEvent(new BusTrackerEvent(3336,"E1",1626,"2018-01-20 11:27:33.480",29.2564,91.09118));
//		engine.getEPRuntime().sendEvent(new BusTrackerEvent(3336,"E1",1626,"2018-01-20 11:28:33.117",29.2678,91.09304));
//		engine.getEPRuntime().sendEvent(new BusTrackerEvent(12022,"E3",4608,"2018-01-20 09:05:34.510",29.1236,91.09028));
		
	}		
}
