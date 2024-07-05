package main.java.org.ortega.server;

import main.java.org.ortega.bankapp.OperationsBancaires;
import main.java.org.ortega.bankapp.OperationsBancairesHelper;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

public class CORBAServer {

    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);
            // Activation POA
            POA poa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
            poa.the_POAManager().activate();
            // Enregistrer le servant dans ORB
            BankServant bankServant = new BankServant();
            bankServant.setOrb(orb);
            // Avoir la référence du servant
            Object ref = poa.servant_to_reference(bankServant);
            OperationsBancaires href = OperationsBancairesHelper.narrow(ref);
            // Faire appel au service CORBA
            Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
            NameComponent path[] = ncRef.to_name("OperationsBancaire");
            // Demarrer la communication
            ncRef.rebind(path, href);
            System.out.println("Server ready and waiting ...");
            orb.run();

        } catch (Exception e) {
            System.out.println("Erreur lors de l'ouverture du serveur " + e);
        }
    }

}
