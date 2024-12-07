package game;

import entities.Entity;
import entities.Poison;
import entities.Position;
import entities.Character;
import entities.characters.Magician;
import staticClasses.Colors;

import java.util.LinkedList;
import java.util.List;

public class Field {
    private int width;
    private int height;

    private Entity[][] entities;

    private List<Entity> unpositionedEntities = new LinkedList<>();

    public Field(int width, int height) {
        this.width = width;
        this.height = height;

        entities = new Entity[height][width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String[][] GetField() {
        String[][] field = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                field[i][j] = entities[i][j] != null ? entities[i][j].getSymbol() : "-";
            }
        }
        return field;
    }

    public void SetEntity(Entity entity) {
        if (GetEntityByPosition(entity.Position) == null) {
            entities[entity.Position.y][entity.Position.x] = entity;
        } else if (entity instanceof Poison) {
            unpositionedEntities.add(entity);
            if (GetEntityByPosition(entity.Position) instanceof Character character) {
                character.IsPoisoned = true;
            }
        }
    }

    public void SetEntity(Entity entity, String color) {
        if (GetEntityByPosition(entity.Position) == null) {
            entity.setSymbolColor(color);
            entities[entity.Position.y][entity.Position.x] = entity;
        } else if (entity instanceof Poison) {
            unpositionedEntities.add(entity);
            if (GetEntityByPosition(entity.Position) instanceof Character character) {
                character.IsPoisoned = true;
            }
        }
    }

    public Position[] GetEntityPositions() {
        Position[] positions = new Position[countEntities()];

        int i = 0;
        for (Entity[] entityRow : entities) {
            for (Entity entity : entityRow) {
                if (entity != null) {
                    positions[i++] = entity.Position;
                }
            }
        }

        return positions;
    }

    public void Tick() {
        for (Entity[] entityRow : entities) {
            for (Entity entity : entityRow) {
                if (entity != null) {
                    entity.Tick();
                    if (!entity.CheckLifeTime()) {
                        entities[entity.Position.y][entity.Position.x] = null;
                    }
                }
            }
        }

        for (Entity entity : unpositionedEntities) {
            entity.Tick();
            if (!entity.CheckLifeTime()) {
                if (GetEntityByPosition(entity.Position) instanceof Character character) {
                    character.IsPoisoned = false;
                }
                unpositionedEntities.remove(entity);
            }
        }
    }

    public ActionResultType ExecuteAction(ActionParams params, Player player1, Player player2) {
        switch (params.BattleAction) {
            case ActionType.Move:
                return MoveEntity(params.Character, params.Destination);
            case ActionType.Damage:
                int[] damageValue = new int[1];
                ActionResultType actionResult = params.Character
                        .TryDamageOther(params.Destination, this, damageValue);
                params.Value = damageValue[0];

                if (actionResult == ActionResultType.Killed) {
                    KillCharacter((Character) GetEntityByPosition(params.Destination), player1, player2);
                }

                return actionResult;
            case ActionType.Heal:
                if (params.Character instanceof Magician healer) {
                    int[] healedValue = new int[1];
                    ActionResultType art = healer.Heal((Character) GetEntityByPosition(params.Destination), healedValue);
                    params.Value = healedValue[0];
                    return art;
                }
                break;
        }
        return ActionResultType.Incomplete;
    }

    public void KillCharacter(Character character, Player player1, Player player2) {
        for (int i = 0; i < player1.characters.length; i++) {
            if (player1.characters[i].equals(character)) {
                Character[] newCharacters = new Character[player1.characters.length - 1];

                int k = 0;
                for (int j = 0; j < player1.characters.length; j++) {
                    if (j != i) {
                        newCharacters[k++] = player1.characters[j];
                    }
                }
                player1.characters = newCharacters;
                break;
            }
        }
        for (int i = 0; i < player2.characters.length; i++) {
            if (player2.characters[i].equals(character)) {
                Character[] newCharacters = new Character[player2.characters.length - 1];

                int k = 0;
                for (int j = 0; j < player2.characters.length; j++) {
                    if (j != i) {
                        newCharacters[k++] = player2.characters[j];
                    }
                }
                player2.characters = newCharacters;
                break;
            }
        }
        entities[character.Position.y][character.Position.x] = null;
    }

    public Entity GetEntityByPosition(Position pos) {
        if (pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height) {
            return entities[pos.y][pos.x];
        }
        return null;
    }

    private ActionResultType MoveEntity(Entity entity, Position newPosition) {
        if (newPosition.x < width && newPosition.y < height && newPosition.x >= 0 && newPosition.y >= 0) {
            if (GetEntityByPosition(newPosition) == null) {
                entities[entity.Position.y][entity.Position.x] = null;

                if (entity instanceof Character character) {
                    if (character.IsPoisoned) {
                        character.IsPoisoned = false;

                        for (Entity unposEntity : unpositionedEntities) {
                            if (unposEntity instanceof Poison poison) {
                                if (poison.Position.Equals(entity.Position)) {
                                    unpositionedEntities.remove(unposEntity);
                                    SetEntity(poison, Colors.GREEN);
                                }
                            }
                        }

                    }
                }

                entity.Position.Set(newPosition);
                entities[entity.Position.y][entity.Position.x] = entity;

                entity.DoAction();

                return ActionResultType.Success;
            } else if (GetEntityByPosition(newPosition) instanceof Poison poison) {
                return ActionResultType.PoisonReached;
            }
        }
        return ActionResultType.Incomplete;
    }

    private int countEntities() {
        int count = 0;

        for (Entity[] entityRow : entities) {
            for (Entity entity : entityRow) {
                if (entity != null) {
                    count++;
                }
            }
        }

        return count;
    }
}
