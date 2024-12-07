package game;

import entities.Position;
import entities.Character;
import entities.Rock;
import entities.characters.Barbarian;
import entities.characters.Magician;
import entities.characters.Witch;
import staticClasses.Colors;
import staticClasses.RandomRangesConfig;
import staticClasses.Symbols;

import java.io.Console;

public class Game {
    private final int INTERFACE_WIDTH = 71;

    private final int FIELD_WIDTH = 5;
    private final int FIELD_HEIGHT = 5;

    private final Character[] CHARACTERS_PRESET_PLAYER_1 = new Character[3];
    private final Character[] CHARACTERS_PRESET_PLAYER_2 = new Character[3];

    private final int ROCK_COUNT = 4;

    private static final String PLAYER_1_COLOR = Colors.BLUE;
    private static final String PLAYER_2_COLOR = Colors.RED;

    private Field field;
    private Player player1;
    private Player player2;


    public Game() {
        field = new Field(FIELD_WIDTH, FIELD_HEIGHT);
    }

    public void Start() {
        WelcomeText();

        String[] names = PlayersNaming();

        UpdatePresets();

        for (Character character : CHARACTERS_PRESET_PLAYER_1) {
            field.SetEntity(character, PLAYER_1_COLOR);
        }
        for (Character character : CHARACTERS_PRESET_PLAYER_2) {
            field.SetEntity(character, PLAYER_2_COLOR);
        }

        player1 = new Player(names[0], CHARACTERS_PRESET_PLAYER_1, PLAYER_1_COLOR);
        player2 = new Player(names[1], CHARACTERS_PRESET_PLAYER_2, PLAYER_2_COLOR);

        FillFieldWithRocks(ROCK_COUNT);

        GameProcess();
    }

    private void WelcomeText() {
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.Title("Welcome to the Battle!");
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("Here is some rules", new String[]{
                "game requires two players",
                "each player must use every character in turn",
                "every player has three characters to choose",
                "once you choose the character, choose the action it must do",
                "every selection option has number or letter, enter it to choose the option",
                "every thing you do (tick) impacts on every object lifetime, increasing it",
                "example to previous - poison leaves only 3 ticks, so after 3 things on field it disappears"
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.SubTitle("About mechanics");
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("General", new String[]{
                "character can move only on empty cell",
                "friendly fire is available, so BE CAREFUL!",
                "if you miss, you lose character action, so enter coordinates correctly"
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("Barbarian " + Colors.Format(Symbols.BARBARIAN, Colors.YELLOW), new String[]{
                "can hit only if enemy right next to him (diagonals exclude)",
                "Damage range: " + RandomRangesConfig.BARBARIAN_DAMAGE_RANGE_MIN + "-" + RandomRangesConfig.BARBARIAN_DAMAGE_RANGE_MAX
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("Magician " + Colors.Format(Symbols.MAGICIAN, Colors.YELLOW), new String[]{
                "can heal other characters",
                "deal distance damage",
                "Damage range: " + RandomRangesConfig.MAGICIAN_DAMAGE_RANGE_MIN + "-" + RandomRangesConfig.MAGICIAN_DAMAGE_RANGE_MAX
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("Witch " + Colors.Format(Symbols.WITCH, Colors.YELLOW), new String[]{
                "can cast poison on any cell",
                "if hits character - cast poison on him",
                "Damage range: " + RandomRangesConfig.WITCH_DAMAGE_RANGE_MIN + "-" + RandomRangesConfig.WITCH_DAMAGE_RANGE_MAX
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("Poison " + Colors.Format(Symbols.POISON, Colors.GREEN), new String[]{
                "character under poison effect can not be healed",
                "after character moving, poison stays on field to the end of it's life (3 tick)",
                "Damage range: " + RandomRangesConfig.POISON_DAMAGE_RANGE_MIN + "-" + RandomRangesConfig.POISON_DAMAGE_RANGE_MAX
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.List("Rock " + Colors.Format(Symbols.ROCK, Colors.WHITE), new String[]{
                "obstacle for movement"
        });
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.GetInput("Enter any letters to continue...");
    }

    private String[] PlayersNaming() {
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.SubTitle("Players naming");
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.Paragraph("so now you need to select name for both of players. be careful," +
                " this may be the name of winner, and.. you have to see these names to the end of the game");
        ConsoleManager.Instance.EmptyLine();
        String name1 = ConsoleManager.Instance.GetInput("Enter name of first player");
        ConsoleManager.Instance.EmptyLine();
        String name2 = ConsoleManager.Instance.GetInput("Enter name of second player");

        return new String[]{name1, name2};
    }

    private void GameProcess() {
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.Title("Let's start the game!");
        ConsoleManager.Instance.EmptyLine();

        boolean turn = true;
        Player playerTurn;

        while (true) {
            playerTurn = turn ? player1 : player2;

            int actionableCharacters = playerTurn.CountActionableCharacters();

            while (actionableCharacters > 0) {
                ConsoleManager.Instance.Paragraph("Turn " + (turn ? player1 : player2));
                ConsoleManager.Instance.FieldInterface(INTERFACE_WIDTH, field, player1, player2);

                ActionParams params = AskAction(playerTurn);

                ActionResultType actionResult;
                if (params.Character.IsActionable()) {
                    actionResult = field.ExecuteAction(params, player1, player2);
                } else {
                    actionResult = ActionResultType.AlreadyUsed;
                }

                String message;
                switch (actionResult) {
                    case ActionResultType.Success:
                        ConsoleManager.Instance.SubTitle("Success!");
                        ConsoleManager.Instance.EmptyLine();
                        break;
                    case ActionResultType.Unsuccessful:
                        ConsoleManager.Instance.Error("Unsuccessful!");
                        ConsoleManager.Instance.EmptyLine();
                        continue;
                    case ActionResultType.InvalidDirection:
                        ConsoleManager.Instance.Error("Invalid direction!");
                        ConsoleManager.Instance.EmptyLine();
                        continue;
                    case ActionResultType.Damaged:
                        ConsoleManager.Instance.SubTitle("Your character deal " + params.Value + " damage!");
                        ConsoleManager.Instance.EmptyLine();
                        break;
                    case ActionResultType.Killed:
                        ConsoleManager.Instance.SubTitle("Character died!");
                        ConsoleManager.Instance.EmptyLine();
                        break;
                    case ActionResultType.PoisonReached:
                        ConsoleManager.Instance.Error("Character is afraid of poison!");
                        ConsoleManager.Instance.EmptyLine();
                        continue;
                    case ActionResultType.AlreadyUsed:
                        ConsoleManager.Instance.Error("Character is already used!");
                        ConsoleManager.Instance.EmptyLine();
                        continue;
                    case ActionResultType.Missed:
                        ConsoleManager.Instance.SubTitle("Character missed!");
                        ConsoleManager.Instance.EmptyLine();
                        break;
                    case ActionResultType.Healed:
                        ConsoleManager.Instance.SubTitle("Your character heal " + params.Value + " hp!");
                        ConsoleManager.Instance.EmptyLine();
                        break;
                }

                field.Tick();

                for (Character character : player1.characters) {
                    if (!character.IsAlive()) {
                        field.KillCharacter(character, player1, player2);
                        ConsoleManager.Instance.SubTitle("Character died!");
                        ConsoleManager.Instance.EmptyLine();
                    }
                }
                for (Character character : player2.characters) {
                    if (!character.IsAlive()) {
                        field.KillCharacter(character, player1, player2);
                        ConsoleManager.Instance.SubTitle("Character died!");
                        ConsoleManager.Instance.EmptyLine();
                    }
                }

                if (GameHasWinners()) {
                    return;
                }
                actionableCharacters = playerTurn.CountActionableCharacters();
            }

            turn = !turn;
            for (Character character : playerTurn.characters) {
                character.MakeActionable();
            }
        }
    }

    private ActionParams AskAction(Player player) {
        ActionParams resultActionParams = new ActionParams();

        ConsoleManager.Instance.EmptyLine();
        int characterChoice = ConsoleManager.Instance.Selection("Chose character",
                new String[0], "(1 - " + player.characters.length + ")", player.characters.length);
        resultActionParams.Character = player.characters[characterChoice - 1];

        if (!resultActionParams.Character.IsActionable()) {
            return resultActionParams;
        }

        ActionType[] actionTypes = resultActionParams.Character.GetActionTypes();
        String[] actions = new String[actionTypes.length];
        for (int i = 0; i < actionTypes.length; i++) {
            actions[i] = actionTypes[i].toString();
        }

        ConsoleManager.Instance.EmptyLine();
        int battleAction = ConsoleManager.Instance.Selection("Select action",
                actions, "", ActionType.values().length);

        String selectedAction = actions[battleAction - 1];
        switch (selectedAction) {
            case "Damage":
                resultActionParams.BattleAction = ActionType.Damage;
                break;
            case "Heal":
                resultActionParams.BattleAction = ActionType.Heal;
                break;
            case "Move":
                resultActionParams.BattleAction = ActionType.Move;
                break;
        }

        resultActionParams.Value = resultActionParams.Character.GenerateDamageValue();

        boolean paramsCompleted = false;
        while (!paramsCompleted) {
            switch (resultActionParams.BattleAction) {
                case ActionType.Move:
                    String direction = ConsoleManager.Instance.GetInput("u - up\nd - down\nl - left\nr - right");
                    if (!"udlr".contains(direction)) {
                        ConsoleManager.Instance.Error("Invalid value");
                        continue;
                    }

                    Position movementVector = Position.ZERO;
                    switch (direction) {
                        case "u":
                            movementVector = Position.UP;
                            break;
                        case "d":
                            movementVector = Position.DOWN;
                            break;
                        case "l":
                            movementVector = Position.LEFT;
                            break;
                        case "r":
                            movementVector = Position.RIGHT;
                            break;
                    }
                    resultActionParams.Destination = resultActionParams.Character.Position.Add(movementVector);
                    paramsCompleted = true;
                    break;
                case ActionType.Damage:
                    int selectedRow;
                    try {
                        selectedRow = Integer.parseInt(ConsoleManager.Instance.GetInput("Enter row number")) - 1;
                    } catch (NumberFormatException e) {
                        ConsoleManager.Instance.Error("Invalid value");
                        continue;
                    }
                    if (selectedRow < 0 || selectedRow >= FIELD_HEIGHT) {
                        ConsoleManager.Instance.Error("Invalid value");
                        continue;
                    }
                    int selectedColumn = Symbols.LOWER_ALPHABET.indexOf(ConsoleManager.Instance.GetInput("Enter column character"));
                    if (selectedColumn < 0 || selectedColumn >= FIELD_WIDTH) {
                        ConsoleManager.Instance.Error("Invalid value");
                        continue;
                    }
                    resultActionParams.Destination = new Position(selectedColumn, selectedRow);
                    paramsCompleted = true;
                    break;
                case ActionType.Heal:
                    int characterToHeal = ConsoleManager.Instance.Selection("Choose character to heal",
                            new String[0], "(1 - " + player.characters.length + ")", player.characters.length);
                    resultActionParams.Destination = player.characters[characterToHeal - 1].Position;
                    paramsCompleted = true;
                    break;
            }
        }

        return resultActionParams;
    }

    private boolean GameHasWinners() {
        if (player1.characters.length == 0) {
            DeclareWinner(player2);
            return true;
        } else if (player2.characters.length == 0) {
            DeclareWinner(player1);
            return true;
        }
        return false;
    }

    private void DeclareWinner(Player player) {
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.FieldInterface(INTERFACE_WIDTH, field, player1, player2);
        ConsoleManager.Instance.EmptyLine();
        ConsoleManager.Instance.Title("Game Over!");
        ConsoleManager.Instance.Title(Colors.Reformat(player.Name) + " is Winner!!");
    }

    private void UpdatePresets() {
        CHARACTERS_PRESET_PLAYER_1[0] = new Barbarian(new Position(0, FIELD_HEIGHT / 2), Symbols.BARBARIAN);
        CHARACTERS_PRESET_PLAYER_1[1] = new Magician(new Position(0, 0), Symbols.MAGICIAN);
        CHARACTERS_PRESET_PLAYER_1[2] = new Witch(new Position(0, FIELD_HEIGHT - 1), Symbols.WITCH);

        CHARACTERS_PRESET_PLAYER_2[0] = new Barbarian(new Position(FIELD_WIDTH - 1, FIELD_HEIGHT / 2), Symbols.BARBARIAN);
        CHARACTERS_PRESET_PLAYER_2[1] = new Magician(new Position(FIELD_WIDTH - 1, 0), Symbols.MAGICIAN);
        CHARACTERS_PRESET_PLAYER_2[2] = new Witch(new Position(FIELD_WIDTH - 1, FIELD_HEIGHT - 1), Symbols.WITCH);
    }

    private void FillFieldWithRocks(int count) {
        if (field != null) {
            Position[] entityPositions = field.GetEntityPositions();
            Position[] rockPositions = new Position[count];
            int actualRockCount = 0;

            while (count > 0) {
                int randX = (int) (Math.random() * FIELD_WIDTH);
                int randY = (int) (Math.random() * FIELD_HEIGHT);

                Position rockPosition = new Position(randX, randY);

                boolean canAdd = true;

                for (Position pos : entityPositions) {
                    if (pos.Distance(rockPosition) <= 1) {
                        canAdd = false;
                        break;
                    }
                }

                for (int i = 0; canAdd && i < actualRockCount; i++) {
                    if (rockPositions[i].Equals(rockPosition)) {
                        canAdd = false;
                        break;
                    }
                }

                if (canAdd) {
                    field.SetEntity(new Rock(rockPosition, Symbols.ROCK));
                    rockPositions[actualRockCount++] = rockPosition;
                    count--;
                }
            }
        }
    }
}
