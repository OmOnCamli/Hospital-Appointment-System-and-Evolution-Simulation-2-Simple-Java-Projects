import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Interface for mating behaviors
interface MatingBehavior {
    public Creature mate(Creature partner) throws Exception;
}

// Canlı sınıfı
class Creature {
    private String species;
    private int age;
    private int fitness;
    private boolean alive;

    public Creature(String species, int age, int fitness) {
        this.species = species;
        this.age = age;
        this.fitness = fitness;
        this.alive = true;
    }

    public void ageOneYear() {
        age++;
        //Yaşlandıkça ölme ihtimali artsın
        Random random = new Random();
        double deathChance = (double) age / 100; // Yaşa göre ölüm şansı (max %100)
        if (random.nextDouble() < deathChance || age > 10) {
            alive = false;
            System.out.println(species + " öldü (yaş: " + age + ", fitness: " + fitness + ")");
        }
    }

    public String getSpecies() {
        return species;
    }

    public int getAge() {
        return age;
    }

    public int getFitness() {
        return fitness;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public String toString() {
        return species + ", Age: " + age + ", Fitness: " + fitness + ", Alive: " + alive;
    }
}

// İnsan sınıfı (Canlı'dan türetilmiş)
class Human extends Creature implements MatingBehavior {
    private int intelligence;

    public Human(String species, int age, int fitness, int intelligence) {
        super(species, age, fitness);
        this.intelligence = intelligence;
    }

    public Creature mate(Creature partner) throws Exception {
        if (partner instanceof Human) {
            Human partnerHuman = (Human) partner;
            int childFitness = (int) ((this.getFitness() + partnerHuman.getFitness()) / 2.0 + (Math.random() * 2 - 1)); //Ortalama fitness + ufak rastgelelik
            childFitness = Math.max(1, Math.min(10, childFitness)); // 1-10 arası sınırla
            int childIntelligence = (int) ((this.intelligence + partnerHuman.intelligence) / 2.0 + (Math.random() * 20 - 10)); // Ort. zeka + rastgelelik
            childIntelligence = Math.max(1, Math.min(100, childIntelligence));  // 1-100 arası sınırla

            return new Human("Human", 0, childFitness, childIntelligence);
        } else {
            throw new Exception("Incompatible species for mating");
        }
    }

    public String toString() {
        return "Human, Age: " + getAge() + ", Fitness: " + getFitness() + ", Intelligence: " + intelligence + ", Alive: " + isAlive();
    }
}

// Hayvan sınıfı (Canlı'dan türetilmiş)
class Animal extends Creature implements MatingBehavior {
    private int speed;

    public Animal(String species, int age, int fitness, int speed) {
        super(species, age, fitness);
        this.speed = speed;
    }

    public Creature mate(Creature partner) throws Exception {
        if (partner instanceof Animal) {
            Animal partnerAnimal = (Animal) partner;
            int childFitness = (int) ((this.getFitness() + partnerAnimal.getFitness()) / 2.0 + (Math.random() * 2 - 1)); //Ortalama fitness + ufak rastgelelik
            childFitness = Math.max(1, Math.min(10, childFitness)); // 1-10 arası sınırla
            int childSpeed = (int) ((this.speed + partnerAnimal.speed) / 2.0 + (Math.random() * 20 - 10)); // Ort. hiz + rastgelelik
            childSpeed = Math.max(1, Math.min(100, childSpeed));  // 1-100 arası sınırla
            return new Animal("Animal", 0, childFitness, childSpeed);
        } else {
            throw new Exception("Incompatible species for mating");
        }
    }

    public String toString() {
        return "Animal, Age: " + getAge() + ", Fitness: " + getFitness() + ", Speed: " + speed + ", Alive: " + isAlive();
    }
}

// Popülasyon sınıfı (İç içe class)
class Population {
    private List<Creature> creatures = new ArrayList<>();

    public void addCreature(Creature creature) {
        creatures.add(creature);
    }

    public void simulateYear() {
        for (Creature creature : new ArrayList<>(creatures)) { // ConcurrentModificationException'ı önlemek için kopyası üzerinde dön
            creature.ageOneYear();
            if (!creature.isAlive()) {
                creatures.remove(creature);
            }
        }
    }

    public void showPopulation() {
        for (Creature creature : creatures) {
            System.out.println(creature);
        }
    }

    public void createOffspring(Creature parent1, Creature parent2) {
        try {
            if (parent1 instanceof MatingBehavior && parent1.isAlive() && parent2.isAlive()) {
                Creature offspring = ((MatingBehavior) parent1).mate(parent2);
                addCreature(offspring);
                System.out.println("Offspring created: " + offspring);
            } else {
                System.out.println("Ebeveynlerden biri çiftleşemez veya hayatta değil.");
            }
        } catch (Exception e) {
            System.out.println("Error during mating: " + e.getMessage());
        }
    }

    // Dışarıdan sadece kopyasını veriyoruz, direkt listeyi değil.
    public List<Creature> getCreatures() {
        return new ArrayList<>(creatures);  // Listenin kopyasını döndür
    }
}

// Ana sınıf (Simülasyon başlatılır)
public class EvolutionSimulation {
    public static void main(String[] args) {
        Population population = new Population();

        // Canlılar ekleyelim
        Human human1 = new Human("Human", 0, 7, 80);
        Human human2 = new Human("Human", 0, 6, 85);
        Animal animal1 = new Animal("Animal", 0, 8, 50);
        Animal animal2 = new Animal("Animal", 0, 6, 45);

        population.addCreature(human1);
        population.addCreature(human2);
        population.addCreature(animal1);
        population.addCreature(animal2);

        // Popülasyonu simüle edelim
        for (int i = 0; i < 15; i++) {
            System.out.println("Yıl: " + (i + 1));
            population.simulateYear();
            System.out.println("Popülasyon:");
            population.showPopulation();

            // Çiftleşme ve yavru oluşturma (rastgele eşleşmeler)
            List<Creature> currentCreatures = population.getCreatures(); //Creature listesini al
            if (currentCreatures.size() >= 2) {
                Random random = new Random();
                int index1 = random.nextInt(currentCreatures.size());
                int index2 = random.nextInt(currentCreatures.size());
                if (index1 != index2) {
                    population.createOffspring(currentCreatures.get(index1), currentCreatures.get(index2));
                } else {
                    if (currentCreatures.size() > 1) {
                        index2 = (index1 + 1) % currentCreatures.size();
                        population.createOffspring(currentCreatures.get(index1), currentCreatures.get(index2));
                    }
                }
            }

            System.out.println("--------------------");
        }

        // Sonuçları göster
        System.out.println("Simülasyon Sonucu:");
        population.showPopulation();
    }
}