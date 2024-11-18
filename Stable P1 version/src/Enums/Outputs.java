/**
 * Enum Outputs
 * @Authors Francisco Correia 67264 & Sérgio Garrido 67202
 */


package Enums;


/**
 * Enum Outputs responsible for supporting the integration of outputs into main
 */
public enum Outputs {
    /**
     *
     * Output commands
     */
    FILE("savestate.txt"),
    EXISTANT_LINE("Linha existente."),
    NONEXISTANT_LINE("Linha inexistente."),
    INSERT_LINE_OK("Inserção de linha com sucesso."),
    REMOVE_LINE_OK("Remoção de linha com sucesso."),
    INVALID_SCHEDULE("Horário inválido."),
    NONEXISTANT_SCHEDULE("Horário inexistente."),
    SCHEDULE_INSERT_OK("Criação de horário com sucesso."),
    SCHEDULE_REMOVE_OK("Remoção de horário com sucesso."),
    NONEXISTANT_START_STATION("Estação de partida inexistente."),
    IMPOSSIBLE_ROUTE("Percurso impossível."),
    STATION_HOUR("%s %02d:%02d\n"),
    APP_TERM("Aplicação terminada."),
    SAVE_ERROR("Erro a salvar.");


    /**
     * Returns the value of the output
     * @return A string representing the output
     */
    public String getString(){
        return s;
    }

    String s;

    /**
     * Constructor
     * @param s - The string output
     */
    Outputs(String s) {
        this.s = s;
    }
}


/**
 * End of Enum Outputs
 */
