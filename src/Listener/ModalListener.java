package Listener;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ModalListener extends ListenerAdapter {

	@Override
	public void onModalInteraction(ModalInteractionEvent event) {
		// TODO Auto-generated method stub
		if(event.getModalId().equals("grind-modal")) {
			String duration = event.getValue("durationModal").getAsString();
			String numberOfMobsKilled = event.getValue("numberModal").getAsString();
			String goldMesos = event.getValue("goldMesosModal").getAsString();
			String redMesos = event.getValue("redMesosModal").getAsString();
			String exp = event.getValue("expModal").getAsString();

			event.reply("Sup, " + duration).queue();
		}
	}

	
}
