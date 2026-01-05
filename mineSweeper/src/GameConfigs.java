public class GameConfigs {
        public final String playerName;
        public final int gridSize;
        public final int bombCount;
        public final boolean cheats;

        public GameConfigs(String playerName, int gridSize, int bombCount, boolean cheats) {
            this.playerName = playerName;
            this.gridSize = gridSize;
            this.bombCount = bombCount;
            this.cheats = cheats;
        }

}
