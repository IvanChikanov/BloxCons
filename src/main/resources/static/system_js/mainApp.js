import { HTMLFather as HTML, HTMLFather } from "../system_js/HTMLFather.js";
import { MainGrid } from "../system_js/mainGrid.js";
import { App as AppSuper } from "../system_js/mainApp.min.js";
class App extends AppSuper
{
    timeoutId;
    framesId;
    sizeTools = 1;
    percents = -50;
    unhide;
    constructor(data)
    {
        super(data);
        this.createGridPanel();
        this.toolsHider();
    }
    createGrid(gridData)
    {
        new MainGrid(gridData);
    }
    createGridPanel()
    {
        this.tools = HTML.createAndAppend("DIV");
        HTML.addStyles(["grid","fixed", "boxShadowCenter","opacityBlack", "leftSticky", "gapFivePix", "padding5px", "bRadius04rem", "zIndex500"], [this.tools]);
        let theme = HTML.createAndAppend("BUTTON", this.tools);
        theme.innerText = "Выбор темы";
        theme.onclick = ()=>{
            let popup = HTML.createPopup();
            let bDiv = HTML.createAndAppend("DIV", popup);
            HTML.addStyles(["grid", "gapFivePix"], [bDiv]);
            let green = HTML.createAndAppend("BUTTON", bDiv);
            green.innerText = "Зеленый";
            green.dataset.color = "green";
            green.onclick = this.changeTheme;
            let blue = HTML.createAndAppend("BUTTON", bDiv);
            blue.innerText = "Синий";
            blue.dataset.color = "blue";
            blue.onclick = this.changeTheme;
            let orang = HTML.createAndAppend("BUTTON", bDiv);
            orang.innerText = "Оранжевый";
            orang.dataset.color = "orange";
            orang.onclick = this.changeTheme;
            let old = HTML.createAndAppend("BUTTON", bDiv);
            old.innerText = "2021";
            old.dataset.color = "old";
            old.onclick = this.changeTheme;};
        let pages = HTML.createAndAppend("BUTTON", this.tools);
        pages.innerText = "Страницы";
        pages.onclick = ()=>{
            let popup = HTML.createPopup();
            popup.appendChild(MainGrid.deletePage());
            let count = HTML.createAndAppend("INPUT", popup);
            let store = {};
            let goButton = HTML.createAndAppend("BUTTON", popup);
            goButton.innerText = "Создать страницу";
            goButton.addEventListener("click", async ()=>{HTML.getValueAction(count, store, "count"); new MainGrid(await HTML.getJSON(["save_mg", this.id, store.count])); MainGrid.allGrids[MainGrid.activePage].setActive(); document.body.removeChild(popup.parentNode);});
        };
        let mathjax = HTML.createAndAppend("BUTTON", this.tools);
        mathjax.innerText = "Math";
        if(this.isMath)
        {
            mathjax.classList.add("bactive");
        }
        mathjax.onclick = (e)=>
        {
            this.isMath = this.isMath?false:true;
            e.target.classList.toggle("bactive");
            this.save();
        }
        let showJSON = HTML.createAndAppend("BUTTON", this.tools);
        showJSON.innerText = "Скачать ZIP";
        showJSON.onclick = ()=>
        {
           fetch(`/loadzip/${this.id}`).then(result => result.blob()).then(blob => {
            let b = URL.createObjectURL(blob);
            let a = HTML.create("a");
            a.href = b;
            a.download = `${this.name}.zip`;
            a.click();
           }).catch( exept => console.log(exept));
        }
        this.tools.style.transformOrigin = "left";
    }
    changeTheme(e)
    {
        let rootStyles = getComputedStyle(document.documentElement);
        this.theme(e.target.dataset.color);
        let changeThemeEvent = new CustomEvent("changeTheme", 
        { detail: { 
            main: rootStyles.getPropertyValue('--mainColor'),
            second: rootStyles.getPropertyValue('--secondColor')
        }});
        window.dispatchEvent(changeThemeEvent);
        this.save();
    }
    async save()
    {
        let saveData = {
            id: this.id,
            name: this.name,
            theme: this.THEME,
            math: this.isMath,
            grids: []  
        };
        await HTML.sendJSON(["save_page"], saveData);
    }
    toolsHider()
    {
        this.timeoutId = setTimeout(()=>{
            this.unhide = HTMLFather.create("BUTTON");
            this.unhide.style = "position: fixed; bottom: 5px; left: 5px; width: 50px; height: 50px; border-radius: 25px; background:#ffffff29; backdrop-filter: blur(1px); box-shadow: 0 0 4px 2px gray;";
            document.body.appendChild(this.unhide);
            this.unhide.onclick = ()=>{
                this.tools.style = "transform-origin: left;";
                this.toolsHider();
                document.body.removeChild(this.unhide);
            };
            this.framesId = requestAnimationFrame(()=>{
                this.animation((q)=>{
                    if(q > 0)
                    {
                        return true;
                    }
                    else
                    {
                        return false
                    };
                }, (size)=> {return size - 0.08}, (pos)=> {return pos + 12})});
        },
        11000);
    }
    animation(predicate, size, pos)
    {
        if(predicate(this.sizeTools))
        {
            this.sizeTools = size(this.sizeTools);
            this.percents = pos(this.percents);
            this.tools.style.transform = `translate(0, ${this.percents}%) scale(${this.sizeTools}, 1)`;
            this.framesId = requestAnimationFrame(()=>{this.animation(predicate, size, pos)});
        }
        else
        {
            cancelAnimationFrame(this.framesId);
            this.sizeTools = 1;
            this.percents = -50;
        }
    }
}
export {App}