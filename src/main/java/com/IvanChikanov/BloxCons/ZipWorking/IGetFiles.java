package com.IvanChikanov.BloxCons.ZipWorking;

import java.io.IOException;
import java.util.Map;

public interface IGetFiles {

    Map<String, byte[]> GetFiles() throws IOException;
}
