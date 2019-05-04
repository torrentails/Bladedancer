package blademaster.cards;

import blademaster.Blademaster;
import blademaster.actions.LoadCardImageAction;
import blademaster.patches.AbstractCardEnum;
import blademaster.powers.BasicStance;
import blademaster.powers.FuryPower;
import blademaster.powers.LightningStance;
import blademaster.powers.WindStance;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public class FuriousStrike extends AbstractStanceCard {


    public static final String ID = Blademaster.makeID("FuriousStrike");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = Blademaster.makePath("cards/FuriousStrike.png");
    public static final String LIMG = Blademaster.makePath("cards/LightningFuriousStrike.png");
    public static final String WIMG = Blademaster.makePath("cards/WindFuriousStrike.png");
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String[] EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;

    private boolean WindArt = false;
    private boolean LightningArt = false;
    private boolean BaseArt = false;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = AbstractCardEnum.DEFAULT_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int AMT = 7;


    public FuriousStrike() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.magicNumber = this.baseMagicNumber = AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new FuryPower(p, this.magicNumber), this.magicNumber));
    }

    @Override
    public void update() {
        super.update();
        if (WindArtS) {
            if (! WindArt) {
                this.loadCardImage(WIMG);
                if (this.upgraded) {
                    System.out.println("I'm out!");
                    this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[2];
                } else {
                    System.out.println("I'm not being registered as upgraded!");
                    this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
                }
                initializeDescription();
                WindArt = true;
                LightningArt = false;
                BaseArt = false;
            }
        } else if (LightningArtS) {
            if (! LightningArt) {
                this.loadCardImage(LIMG);
                if (this.upgraded) {
                    this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[3];
                } else {
                    this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
                }
                initializeDescription();
                WindArt = false;
                LightningArt = true;
                BaseArt = false;
            }
        } else if (CardCrawlGame.isInARun()) {
            if (AbstractDungeon.player.hasPower(WindStance.POWER_ID) && (!AbstractDungeon.getMonsters().areMonstersDead())) {
                if (! WindArt) {
                    this.loadCardImage(WIMG);
                    if (this.upgraded) {
                        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[2];
                    } else {
                        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[0];
                    }
                    initializeDescription();
                    WindArt = true;
                    LightningArt = false;
                    BaseArt = false;
                }
            } else if (AbstractDungeon.player.hasPower(LightningStance.POWER_ID) && (!AbstractDungeon.getMonsters().areMonstersDead())) {
                if (! LightningArt) {
                    AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, LIMG, false));
                    if (this.upgraded) {
                        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[3];
                    } else {
                        this.rawDescription = DESCRIPTION + EXTENDED_DESCRIPTION[1];
                    }
                    initializeDescription();
                    WindArt = false;
                    LightningArt = true;
                    BaseArt = false;
                }
            } else if (AbstractDungeon.player.hasPower(BasicStance.POWER_ID) && (!AbstractDungeon.getMonsters().areMonstersDead())) {
                if (! BaseArt) {
                    AbstractDungeon.actionManager.addToBottom(new LoadCardImageAction(this, IMG, false));
                    this.rawDescription = DESCRIPTION;
                    initializeDescription();
                    WindArt = false;
                    LightningArt = false;
                    BaseArt = true;
                }
            }
        }
    }



    @Override
    public void hover() {
        if (this.cardToPreview1 == null && ! this.bullshit) {
            this.cardToPreview1 = new FuriousStrike();
            this.cardToPreview2 = new FuriousStrike();
            if (this.upgraded || SingleCardViewPopup.isViewingUpgrade) {
                this.cardToPreview1.upgrade();
                this.cardToPreview2.upgrade();
            }
            ((FuriousStrike) this.cardToPreview1).WindArtS = true;
            this.cardToPreview1.update();
            ((FuriousStrike) this.cardToPreview2).LightningArtS = true;
            this.cardToPreview2.update();
        }
        super.hover();
        this.bullshit = true;
    }

    @Override
    public void unhover() {
        super.unhover();
        this.bullshit = false;
        this.cardToPreview1 = null;
        this.cardToPreview2 = null;
    }

    public void renderCardTip(SpriteBatch sb) {
        if ((this.cardToPreview1 != null) && (! Settings.hideCards) && (this.bullshit)) {
            float tmpScale = this.drawScale / 1.5F;

            if ((AbstractDungeon.player != null) && (AbstractDungeon.player.isDraggingCard)) {
                return;
            }

            if (this.current_x > Settings.WIDTH * 0.75F) {
                this.cardToPreview1.current_x = this.current_x + (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (16.0F)) * this.drawScale);
            } else {
                this.cardToPreview1.current_x = this.current_x - (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (16.0F)) * this.drawScale);
            }

            this.cardToPreview1.current_y = this.current_y + ((AbstractCard.IMG_HEIGHT / 2.0F)) * this.drawScale;

            this.cardToPreview1.drawScale = tmpScale;

            this.cardToPreview1.render(sb);

            if (this.current_x > Settings.WIDTH * 0.75F) {
                this.cardToPreview2.current_x = this.current_x + (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (16.0F)) * this.drawScale);
            } else {
                this.cardToPreview2.current_x = this.current_x - (((AbstractCard.IMG_WIDTH / 2.0F) + ((AbstractCard.IMG_WIDTH / 2.0F) / 1.5F) + (16.0F)) * this.drawScale);
            }

            this.cardToPreview2.current_y = this.current_y - ((AbstractCard.IMG_HEIGHT / 6.0F)) * this.drawScale;

            this.cardToPreview2.drawScale = tmpScale;

            this.cardToPreview2.render(sb);
        }
        if ((! Settings.hideCards) && (this.bullshit)) {
            if ((SingleCardViewPopup.isViewingUpgrade) && (this.isSeen) && (! this.isLocked)) {
                AbstractCard copy = makeStatEquivalentCopy();
                copy.current_x = this.current_x;
                copy.current_y = this.current_y;
                copy.drawScale = this.drawScale;
                copy.upgrade();

                TipHelper.renderTipForCard(copy, sb, copy.keywords);
            } else {
                super.renderCardTip(sb);
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new FuriousStrike();
    }

    @Override
    public void upgrade() {
        if (! this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(2);
            this.upgradeMagicNumber(3);
            this.initializeDescription();
        }
    }
}