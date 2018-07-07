package cin.ufpe.if998;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;


public class BusTrackerEventHandler {

	public static void main(String[] args) {
		EPServiceProvider engine = EPServiceProviderManager.getDefaultProvider();
		engine.getEPAdministrator().getConfiguration().addEventType(BusTrackerEvent.class);

		//selecionar os nomes das empresas
		String epla = "select avg(temperature) as temperatureMedia  from TemperatureEvent#length(10)";
		//selecionar os onibus das empresas
		String eplb = "select count(temperature) as quantidade from TemperatureEvent#time(20 sec)";

		String eplc = "select nome from BusTrackerEvent";

		EPStatement statement = engine.getEPAdministrator().createEPL(epla);

		statement.addListener((newData, oldData) -> {
			double temperatureMedia = (double) newData[0].get("temperatureMedia");
			//System.out.println(String.format("A temperatura media nos ultimos 100 eventos � %lf �C.",temperatureMedia));
			System.out.println("A temperatura media nos ultimos 100 eventos �: "+ temperatureMedia+" �C.\n");

		});

		//engine.getEPRuntime().sendEvent(new TemperatureEvent(25));
		//engine.getEPRuntime().sendEvent(new TemperatureEvent(15));
	}
}
