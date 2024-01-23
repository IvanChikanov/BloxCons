import {HTMLFather as HTML} from "../system_js/HTMLFather.js";
import {Cell} from "../system_js/Cell.js"
import {MainGridFather} from "../system_js/mainGrid.min.js";
class MainGrid extends MainGridFather
{
    static deletePage()
    {
        let delBlox = HTML.create("DIV");
        HTML.addStyles(["grid"],[delBlox])
        let list = HTML.createAndAppend("SELECT", delBlox);
        for(let grids of MainGrid.allGrids)
        {
            let o = HTML.createAndAppend("OPTION", list);
            o.innerText = MainGrid.allGrids.indexOf(grids);
            if(MainGrid.activePage === MainGrid.allGrids.indexOf(grids))
            {
                o.selected = true;
            }
        }

        let del = HTML.createAndAppend("BUTTON", delBlox);
        del.innerText = "Удалить";
        del.onclick = ()=>{
            document.body.removeChild(MainGrid.allGrids[list.value].gridElem);
            MainGrid.allGrids.splice(list.value, 1);
            if(MainGrid.allGrids.length > 0)
            {
                MainGrid.activePage = 0;
                MainGrid.allGrids[0].setActive();
            }
        };
        return delBlox;
    }
    createCell(data)
    {
        for(let i = 1; i <= data.cellArray.length; i++)
        {
            this.cellArray.push(new Cell(this, data.cellArray[i-1]));
        }
    }
    async addNextCell(cell)
    {
        this.cellArray.splice(this.cellArray.indexOf(cell) + 1, 0, 
            new Cell(this, await HTML.getJSON(["pages","new_cell", this.id])));        
        this.updateGrid();
    }
    async deleteCell(cell)
    {
        await HTML.deleteRequest(["pages", "delete_cell", cell.id]);
        this.cellArray.splice(this.cellArray.indexOf(cell), 1);
        this.updateGrid();        
    }
    async updateGrid()
    {
        for(let i = 0; i < this.cellArray.length; i++)
        {
            this.cellArray[i].changePosition(i);
        }
        this.gridElem.style.gridTemplateRows = `repeat(${this.cellArray.length}, auto)`;
        HTML.sendJSON(["pages","update_cells"], this.cellArray);

    }
}
export {MainGrid}