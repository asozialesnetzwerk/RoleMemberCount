import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import java.util.Comparator;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("This bot requires exactly two arguments, the id of the guild and the token of your Discord bot.");
            System.exit(1);
        }

        DiscordApi api = new DiscordApiBuilder().setToken(args[1]).login().exceptionally(e -> {
            System.err.println("Make sure the given token is correct.");
            System.exit(1);
            return null;
        }).join();

        Server server = api.getServerById(args[0]).orElseGet(() -> {
            System.err.println("Make sure the given id ('" + args[0] + "') is correct.");
            System.exit(1);
            return null;
        });

        HashMap<Role, Integer> roles = new HashMap<>();

        server.getRoles().forEach(role -> roles.put(role, role.getUsers().size()));

        roles.keySet().stream()
                .sorted(Comparator.comparing(roles::get))
                .forEach(role ->
                        System.out.println(role.getId()
                                + " (" + (role.isEveryoneRole() ? "everyone" : role.getName()) + ") users: "
                                + roles.get(role)));
        System.exit(0);
    }
}
