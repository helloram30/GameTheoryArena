import { strategyExplanation } from '../strategyGlossary';

interface StrategyNameProps {
  name: string;
}

export default function StrategyName({ name }: StrategyNameProps) {
  return (
    <span className="strategyTooltip" tabIndex={0} aria-label={`${name}: ${strategyExplanation(name)}`}>
      <span className="strategyLabel">{name}</span>
      <span className="infoDot" aria-hidden="true">
        i
      </span>
      <span className="strategyPopover" role="tooltip">
        {strategyExplanation(name)}
      </span>
    </span>
  );
}
