#S2I - local

## il y a un moyen pour faire le s2i en local sans passer par openshift ci dessous l'install:
https://github.com/openshift/source-to-image/releases
## pour lancer le s2i en local sur un repo git on lance la commande suivante:

		./s2i build  [repo-projet] [image s2i] [nom de l'imag à générer]

		./s2i build https://github.com/abellaaj/compte registry.redhat.io/redhat-openjdk-18/openjdk18-openshift toto

### si on a deja clonner notre projet on peut lancer le s2i comme ça:

    ./s2i build ~/git/compte  registry.redhat.io/redhat-openjdk-18/openjdk18-openshift toto

### pour s'assurer que tout va bien on lance la commande docker ps et on tombe sur notre image "toto"
### dans le contexte de bpce l'image s2i n'arrive pas à compiler le code de mom projet compte ce qui nous a obliger à :
 - créer un fichier "configuration" à la racin du projet compte
 - déposer le setting.xml de mon environement dans cerépertoire
 - surtout il faut pas oublier de commiter cet modif pour que s2i prend en compte cette configuration

#S2I - OpenShift (regarder src/main/s2i)

on s'est basé sur l'exemple suivant:
https://github.com/jboss-container-images/openjdk/tree/develop/templates

## si on veut appliquer des param sur un template on lance

		oc process -f [template] [param=value] [param=value] ... => on aura le resultat dans la sortie standart

exemple:

		oc process -f openjdk-web-basic-s2i.json APPLICATION_NAME=toto

## si on veux que la sortie du process sera aplliquer directement sur openshift :

	oc process -f openjdk-web-basic-s2i.json APPLICATION_NAME=toto  | oc apply -f -



