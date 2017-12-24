/**
* Daniel Ricci <thedanny09@gmail.com>
*
* Permission is hereby granted, free of charge, to any person
* obtaining a copy of this software and associated documentation
* files (the "Software"), to deal in the Software without restriction,
* including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software,
* and to permit persons to whom the Software is furnished to do so, subject
* to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
* THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
* FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
* IN THE SOFTWARE.
*/

package entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import engine.utils.logging.Tracelog;
import generated.DataLookup;

public class GameTimerEntity extends AbstractGameEntity {
    
    private static final String BOMB_CONSTANT = "TIMER_";

    private List<GameTimerEntity> _timerEntities = new ArrayList();
 
    private int _numeral;
    
    public GameTimerEntity(int numeral) {
        _numeral = numeral;
        setNumeralImpl();
    }   
    
    public int getNumeral() {
        return _numeral;
    }
    
    public void setNumeral(int numeral) {
        
        if(numeral == _numeral) {
            return;
        }
    
        _numeral = numeral;
        
        if(numeral < 0 || numeral > 9) {
            numeral = 0;
            Tracelog.log(Level.WARNING, true, "Game timer entity exceeded expected range, defaulting to an acceptable value");
        }
            
        setNumeralImpl();            
    }

    private void setNumeralImpl() {
        setActiveData(DataLookup.TIMERS.valueOf(BOMB_CONSTANT + _numeral));
    }
    
    @Override public Image getRenderableContent() {
        List<Image> renderableList = _timerEntities.stream().map(z -> z.getRenderableContent()).collect(Collectors.toList());
        int width = renderableList.parallelStream().map(z -> z.getWidth(null)).mapToInt(Integer::intValue).sum();
        int height = renderableList.get(0).getHeight(null);
        
        Image img = new BufferedImage(width,  height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = img.getGraphics();
        //graphics.r
        return null;
    }
}